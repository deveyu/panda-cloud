package com.yukong.panda.auth.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * 根据RoundRobinRule改造
 */
@Slf4j
public class CustomIsolationRule2 extends AbstractLoadBalancerRule {
    @Autowired
    DiscoveryClient discoveryClient;
    @Value("${eureka.instance.metadata-map.routingTag}")
    String version;

    public static final String RULE_KEY = "version";


    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }

    @Override
    public void setLoadBalancer(ILoadBalancer lb) {
        super.setLoadBalancer(lb);
    }

    @Override
    public Server choose(Object key) {
        ILoadBalancer lb = getLoadBalancer();
        if (lb == null) {
            log.warn("no load balancer");
            return null;
        } else {
            Server server = null;
            while (server == null) {
                if (Thread.interrupted()) {
                    return null;
                }
                List<Server> upList = lb.getReachableServers();
                List<Server> allList = lb.getAllServers();
                List<ServiceInstance> targetInstances = null;
                List<Server> servers = null;
                int serverCount = allList.size();
                if (serverCount == 0) {
                    return null;
                }
                //怎样获取该消费者需要调用哪个服务？？？？？？？？？？？？？？因为该消费者可能会调用多个服务，每个服务也会有
                //多个实例
                //这里是否有漏洞呢?假如该消费者调用的服务不是一个
                Server resultServer = upList.get(0);
                //根据serviceId得到服务实例
                List<ServiceInstance> instances = discoveryClient.getInstances(resultServer.getMetaInfo().getServiceIdForDiscovery());

                if (StrUtil.isNotBlank(version)) {
                    targetInstances = instances.stream().filter(
                            instance -> version.equals(instance.getMetadata().get(RULE_KEY)
                            )
                    ).collect(Collectors.toList());
                }
                if (CollUtil.isEmpty(targetInstances) || StrUtil.isBlank(version)) {
                    targetInstances = instances.stream().filter(
                            instance -> {
                                String metadataVersion = instance.getMetadata().get(RULE_KEY);
                                return StrUtil.isEmpty(metadataVersion);
                            }).collect(Collectors.toList());
                }
                if (CollUtil.isNotEmpty(targetInstances)) {


                    //这里又该怎么根据ServiceInstance获取相应的server
                    for (Server up : upList) {
                        InstanceInfo instanceInfo = (InstanceInfo) ReflectUtil.getFieldValue(up, "instanceInfo");
                        //正常情况下只应该启动一个实例
                        ServiceInstance serviceInstance = targetInstances.get(0);
                        EurekaDiscoveryClient.EurekaServiceInstance serviceInstance1 = (EurekaDiscoveryClient.EurekaServiceInstance) serviceInstance;
                        if (instanceInfo.getInstanceId().equalsIgnoreCase(serviceInstance1.getInstanceInfo().getInstanceId())) {
                            servers.add(up);
                        }
                    }
                    return getServer(servers);
                }
            }
        }
        return null;
    }

    /**
     * 随机取一个实例
     */
    private Server getServer(List<Server> upList) {
        int nextInt = RandomUtil.randomInt(upList.size());
        return upList.get(nextInt);
    }

}

