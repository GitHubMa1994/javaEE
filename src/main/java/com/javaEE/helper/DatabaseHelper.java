package com.javaEE.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;




import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.javaEE.util.CollectionUtil;
import com.javaEE.util.PropsUtil;

/**
 * 数据库操作类
 * @author mwb
 */
public final class DatabaseHelper {
	
	private static final Logger LOGGER=LoggerFactory.getLogger(DatabaseHelper.class);
	private static final QueryRunner QUERY_RUNNER;
	private static final ThreadLocal<Connection> CONNECTION_HOLDER;
	private static final BasicDataSource DATA_SOURCE;
	
	private static final String DRIVER;
	private static final String URL;
	private static final String USERNAME;
	private static final String PASSWORD;
	
	static{
		CONNECTION_HOLDER=new ThreadLocal<Connection>();
		QUERY_RUNNER=new QueryRunner();
		
		Properties conf=PropsUtil.loadProps("config.properties");
		DRIVER=PropsUtil.getString(conf, "jdbc.driver");
		URL=PropsUtil.getString(conf, "jdbc.url");
		USERNAME=PropsUtil.getString(conf, "jdbc.username");
		PASSWORD=PropsUtil.getString(conf, "jdbc.password");
		
		DATA_SOURCE=new BasicDataSource();
		DATA_SOURCE.setDriverClassName(DRIVER);
		DATA_SOURCE.setUrl(URL);
		DATA_SOURCE.setUsername(USERNAME);
		DATA_SOURCE.setPassword(PASSWORD);
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			LOGGER.error("can not load jdbc driver",e);
		}
	}
	
	/**
	 * 获取数据库连接
	 */
	public static Connection getConnection(){
		Connection conn=CONNECTION_HOLDER.get();//寻找已经存在的connection
		if (conn==null){
			try {
				conn=DATA_SOURCE.getConnection();
//				conn=DriverManager.getConnection(URL, USERNAME, PASSWORD);
			} catch (SQLException e) {
				LOGGER.error("get connection failure",e);
			}finally{
				CONNECTION_HOLDER.set(conn);//把链接放入ThreadLoacl
			}
		}
		return conn;

	}
	
	/**
	 * 关闭数据库连接
	 */
	public static void closeConnection(){
		Connection conn=CONNECTION_HOLDER.get();//寻找已经存在的connection
		if (conn!=null) {
			try {
				conn.close();
			} catch (SQLException e) {
				LOGGER.error("close connection failure",e);
				throw new RuntimeException(e);
			}finally{
				CONNECTION_HOLDER.remove();//移除ThreadLoacl里的链接
			}
		}
	}
	
	/**
	 * 查询实体类list
	 */
	public static <T> List<T> queryEntityList(Class<T> entityClass,String sql,Object... params){
		List<T> entityList;
		try{
			Connection conn=getConnection();
			entityList=QUERY_RUNNER.query(sql, new BeanListHandler<T>(entityClass),params);
		}catch(SQLException e){
			LOGGER.error("query entiy list failure",e);
			throw new RuntimeException(e);
		}finally{
			closeConnection();
		}
		return entityList;
	}
	
	/**
	 * 查询list 多表查询
	 */
	public static List<Map<String,Object>> executeQuery(String sql,Object... params){
		List<Map<String,Object>> result=null;
		try {
			Connection conn=getConnection();
			result=QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
		} catch (SQLException e) {
			LOGGER.error("execute query failure",e);
			throw new RuntimeException(e);
		}finally{
			closeConnection();
		}
		return result;
	}
		
	/**
	 * 查询单个实体类
	 */
	public static <T> T queryEntity(Class<T> entityClass,String sql,Object... params){
		T entity = null;
		try {
			Connection conn=getConnection();
			entity=QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
			new ScalarHandler<T>("123");
		} catch (SQLException e) {
			LOGGER.error("query entity failure",e);
			throw new RuntimeException(e);
		}finally{
			closeConnection();
		}
		return entity;
	}
	
	/**
	 * 更新操作(insert update delete)
	 */
	public static int executeUpdate(String sql,Object... params){
		int rows=0;
		try{
			Connection conn=getConnection();
			rows=QUERY_RUNNER.update(conn, sql,params);
		}catch(SQLException e){
			LOGGER.error("execute update failure",e);
			throw new RuntimeException(e);
		}finally{
			closeConnection();
		}
		return rows;
	}
	
	/**
	 * 插入实体类
	 */
	public static <T> boolean insertEntity(Class<T> entityClass,Map<String,Object> fieldMap){
		if (CollectionUtil.isEmpty(fieldMap)) {
			LOGGER.error("can not insert entity:fieldMap is empty");
			return false;
		}
		String sql ="INSERT INTO "+getTableName(entityClass)+" SET ";
		StringBuilder columns=new StringBuilder("(");
		StringBuilder values=new StringBuilder("(");
		for(String fieldName:fieldMap.keySet()){
			columns.append(fieldName).append(", ");
			values.append("?, ");
		}
		columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
		values.replace(values.lastIndexOf(", "), values.length(), ")");
		sql+=columns+" VALUES "+values;
		Object[] params=fieldMap.values().toArray();
		return executeUpdate(sql, params)==1;
	}
	
	/**
	 * 更新实体类
	 */
	public static <T> boolean updateEntity(Class<T> entityClass,long id,Map<String,Object> fieldMap){
		if (CollectionUtil.isEmpty(fieldMap)) {
			LOGGER.error("can not update entity:fieldMap is empty");
			return false;
		}
		String sql ="UPDATE "+getTableName(entityClass)+" SET ";
		StringBuilder columns=new StringBuilder();
		for(String fieldName:fieldMap.keySet()){
			columns.append(fieldName).append("=?, ");
		}
		sql+=columns.substring(0, columns.lastIndexOf(", "))+" WHERE id=?";
		List<Object> paramList=new ArrayList<Object>();
		paramList.addAll(fieldMap.values());
		paramList.add(id);
		Object[] params=paramList.toArray();
		return executeUpdate(sql, params)==1;
	}
	
	/**
	 *删除实体类
	 */
	public static <T> boolean deleteEntity(Class<T> entityClass,long id){
		String sql="DELETE FROM "+getTableName(entityClass)+" WHERE id=?";
		return executeUpdate(sql, id)==1;
	}
	
	private static String getTableName(Class<?> entityClass){
		return entityClass.getSimpleName();
	}
} 



