package com.ydc.basepack.mapper;


import com.ydc.basepack.model.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface MessageMapper {
	
    int deleteByPrimaryKey(String messageId);
    
    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(String messageId);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKeyWithBLOBs(Message record);

    int updateByPrimaryKey(Message record);
	
	void changeBrokerMessageStatus(@Param("brokerMessageId")String brokerMessageId, @Param("brokerMessageStatus")String brokerMessageStatus, @Param("updateTime")Date updateTime);

	List<Message> queryBrokerMessageStatus4Timeout(@Param("brokerMessageStatus")String brokerMessageStatus);
	
	List<Message> queryBrokerMessageStatus(@Param("brokerMessageStatus")String brokerMessageStatus);
	
	int update4TryCount(@Param("brokerMessageId")String brokerMessageId, @Param("updateTime")Date updateTime);
	
}