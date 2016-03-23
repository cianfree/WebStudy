package com.bxtpw.study.bit.mapper;

import com.alibaba.fastjson.JSON;
import com.bxtpw.study.bit.domain.Agent;
import com.bxtpw.study.bit.query.AgentQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/11 21:53
 * @since 0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:persist/applicationContext-persist-test.xml")
public class AgentMapperTest {

    @Autowired
    private AgentMapper agentMapper;

    @Test
    public void testInsertAgent() throws Exception {
        agentMapper.insertAgent(new Agent().setIsRegion(true));
        agentMapper.insertAgent(new Agent().setIsProvince(true));
        agentMapper.insertAgent(new Agent().setIsCity(true));
        agentMapper.insertAgent(new Agent().setIsDistrict(true));
        agentMapper.insertAgent(new Agent().setIsSurname(true));
        agentMapper.insertAgent(new Agent().setIsTown(true));
        agentMapper.insertAgent(new Agent().setIsBurg(true));
        // 既是大区代理，又是省代理，市代理
        agentMapper.insertAgent(new Agent().setIsRegion(true).setIsProvince(true).setIsCity(true));
        // 既是大区代理，又是省代理，但不是市级代理
        agentMapper.insertAgent(new Agent().setIsProvince(true));
    }

    @Test
    public void testQueryAgentList() throws Exception {
        AgentQuery query = new AgentQuery();
        // 查询所有
        List<Agent> agentList = agentMapper.queryAgentList(query);
        System.out.println(JSON.toJSONString(agentList));

        /*
        // 查询大区服务商
        query.clearTagCondition().setIsRegion(true);
        agentList = agentMapper.queryAgentList(query);
        System.out.println(JSON.toJSONString(agentList));

        // 查询大区服务商, 且是省服务商
        query.clearTagCondition().setIsRegion(true).setIsProvince(true);
        agentList = agentMapper.queryAgentList(query);
        System.out.println(JSON.toJSONString(agentList));

        // 查询是大区服务商，且不是市级服务商
        query.clearTagCondition().setIsRegion(true).setIsCity(false);
        agentList = agentMapper.queryAgentList(query);
        System.out.println(JSON.toJSONString(agentList));
        */

    }

    @Test
    public void testGetAgentById() {
        System.out.println(JSON.toJSONString(agentMapper.getAgentById(12)));
        System.out.println(JSON.toJSONString(agentMapper.getAgentById(13)));
        System.out.println(JSON.toJSONString(agentMapper.getAgentById(14)));
        System.out.println(JSON.toJSONString(agentMapper.getAgentById(15)));
        System.out.println(JSON.toJSONString(agentMapper.getAgentById(16)));
    }
}