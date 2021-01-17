package com.ydc.basepack;

import com.ydc.basepack.mapper.MessageMapper;
import com.ydc.basepack.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageStoreService {

	@Autowired
	private MessageMapper messageMapper;

}