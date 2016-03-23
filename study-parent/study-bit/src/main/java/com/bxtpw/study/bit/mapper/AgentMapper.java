package com.bxtpw.study.bit.mapper;

import com.bxtpw.study.bit.domain.Agent;
import com.bxtpw.study.bit.query.AgentQuery;

import java.util.List;

/**
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/11 21:38
 * @since 0.1
 */
public interface AgentMapper {

    /**
     * 添加agent对象
     *
     * @param agent
     * @return
     */
    int insertAgent(Agent agent);

    /**
     * 查询Agent
     *
     * @param query
     * @return
     */
    List<Agent> queryAgentList(AgentQuery query);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Agent getAgentById(Integer id);
}
