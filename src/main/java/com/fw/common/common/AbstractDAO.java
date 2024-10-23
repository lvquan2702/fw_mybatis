package com.fw.common.common;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class AbstractDAO {
    protected Logger log = LoggerFactory.getLogger(AbstractDAO.class);
    @Autowired
    private SqlSessionTemplate sqlSession;

    //쿼리를 인쇄하는 함수
    protected void printQueryId(String queryId) {
        if(log.isDebugEnabled()){
            log.debug("\t QueryId  \t:  " + queryId);
        }
    }

    @SuppressWarnings("unchecked")
    protected void printParam(Object param) {



        if(log.isDebugEnabled()){
            log.debug("\t Param  \t:  " + (Map<String,Object>)param);
        }




    }


    //인서트
    public Object insert(String queryId, Object params){
        printQueryId(queryId);
        //printParam(params);
        return sqlSession.insert(queryId, params);
    }

    //업데이트
    public Object update(String queryId, Object params){
        printQueryId(queryId);
        //printParam(params);
        return sqlSession.update(queryId, params);
    }

    //삭제
    public Object delete(String queryId, Object params){
        printQueryId(queryId);
        //printParam(params);
        return sqlSession.delete(queryId, params);
    }

    //한개 값을 리턴  스트링 파라미터
    public Object selectOne(String queryId){
        printQueryId(queryId);

        return sqlSession.selectOne(queryId);
    }

    //오브젝트 파라미터
    public Object selectOne(String queryId, Object params){
        printQueryId(queryId);
        //printParam(params);
        return sqlSession.selectOne(queryId, params);
    }


    @SuppressWarnings("rawtypes")
    public List selectList(String queryId){
        printQueryId(queryId);
        return sqlSession.selectList(queryId);
    }

    @SuppressWarnings("rawtypes")
    public List selectList(String queryId, Object params){
        printQueryId(queryId);

        return sqlSession.selectList(queryId,params);
    }


}
