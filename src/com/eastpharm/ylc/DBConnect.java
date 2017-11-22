package com.eastpharm.ylc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class DBConnect {
	
	
	private static final String DriverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static final String DbAddress = "jdbc:sqlserver://120.26.97.79:1433;databaseName=EbApp";
	private static final String DbAccount = "sa";
	private static final String DbPswd = "Hdyyysfw1";
	protected Connection conn;
    protected PreparedStatement ps;
    protected ResultSet rs;
    protected ResultDataModel rdm;
    
  //��������
    public boolean getConnection(){
    	try {
    		Class.forName(DriverName);
    		conn=DriverManager.getConnection(DbAddress,DbAccount, DbPswd);
    	} catch (ClassNotFoundException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    		return false;
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    		return false;
    	}
    	return true;
    }
	// �ر���Դ
    public boolean closeResource() {
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }
        if(ps!=null){
            try {
                ps.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }
        
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
  //���ӣ��޸ģ�ɾ��
    public int executeUpdate(String sql, Object[] params){
        getConnection();
        int updateRow=0;
        try {
            ps=conn.prepareStatement(sql);
            //���ռλ��
            for(int i=0;i<params.length;i++){
                ps.setObject(i+1, params[i]);
            }
            updateRow = ps.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return updateRow;
    }
    //
    //��ѯ
     public ResultSet executeSQL(String sql, Object[] params){
            getConnection();
            
            try {
                ps=conn.prepareStatement(sql);
                //���ռλ��
                for(int i=0;i<params.length;i++){
                    ps.setObject(i+1, params[i]);
                }
                rs = ps.executeQuery();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return rs;
        }
     
      //login
      //ִ�д��δ�����ֵ�Ĵ洢����
        public JSONObject getLoginMenthod(String usnm,String pswd){
            //����һ�����������ս��
            JSONObject totalCount= new JSONObject();
            rdm = new ResultDataModel();
            //����CallableStatement����
            CallableStatement proc=null;
            String sql="{call dbo.v6_Login(?,?,?)}";
            //��������
            getConnection();
            //CallableStatement����
            try {
                proc=conn.prepareCall(sql);
                //����ռλ��
                //Object [] params={deptno,ename};
                //ֻ���������������
                
                proc.setString(1, usnm);
                proc.setString(2, pswd);
                proc.setInt(3, 1);
                ////�����ݿ������������ע��Ϊjava�е����ͣ����������ת��
                rs = proc.executeQuery();
             
                JSONArray array = new JSONArray();
                ResultSetMetaData metaData = rs.getMetaData();  
    			int columnCount = metaData.getColumnCount();
    			while(rs.next()){
    				JSONObject jsonObject = new JSONObject();
    				for (int i = 1; i < columnCount; i++) {
    					String columnName =metaData.getColumnLabel(i);  
    		            String value = rs.getString(columnName);
    		            
    		            jsonObject.put(columnName, value);
    				}
    				array.add(jsonObject);
    			}
    			if (array.size()>0) {
    				totalCount = rdm.setData(0, "����ɹ�", array);
				}else {
					totalCount = rdm.setData(10003, "�˺Ų����ڻ��������", array);
				}
                //��ȡ���
    			rs.close();
                proc.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                totalCount = rdm.setData(10001, "���ݲ�ѯ���̴���", null);
            }finally{
                this.closeResource();
                if(proc!=null){
                    try {
                        proc.close();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        totalCount = rdm.setData(10002, "���ݿ����Ӵ���", null);
                    }
                }
            }
            return totalCount;
        }
        
        public JSONObject getRegisterMenthod(String usnm,String pswd) {
        	//����һ�����������ս��
            JSONObject totalCount= new JSONObject();
            rdm = new ResultDataModel();
            //����CallableStatement����
            CallableStatement proc=null;
            String sql="{call dbo.v6_Person(?,?)}";
            //��������
            getConnection();
            //CallableStatement����
            try {
                proc=conn.prepareCall(sql); 
                proc.setString(1, "<?xml version=\"1.0\" encoding=\"gb2312\" ?><GRIDDATA><ITEM P_LSM=\"0\" WORK_NO="+usnm+" PASSWORD="+pswd+" /></GRIDDATA>");
                proc.setString(2, "GRIDDATA/ITEM"); 
                rs = proc.executeQuery();
             
                
                ResultSetMetaData metaData = rs.getMetaData();  
    			int columnCount = metaData.getColumnCount();
    			while(rs.next()){
    				
    				for (int i = 1; i < columnCount; i++) {
    					String columnName =metaData.getColumnLabel(i);  
    		            String value = rs.getString(columnName);
    		            totalCount.put(columnName, value);
    				}
    			}
    			int code = Integer.valueOf(totalCount.getString("code"));
    			String mesg = totalCount.getString("message");
    			int ucode = 0;
    			if (code == 1) {
    				ucode = 0;
				}else if (code == 2) {
					ucode = 10004;
				}else if (code == 3) {
					ucode = 10005;
				}
    			totalCount = rdm.setData(ucode, mesg, null);
                //��ȡ���
    			rs.close();
                proc.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                totalCount = rdm.setData(10001, "���ݲ�ѯ���̴���", null);
            }finally{
                this.closeResource();
                if(proc!=null){
                    try {
                        proc.close();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        totalCount = rdm.setData(10002, "���ݿ����Ӵ���", null);
                    }
                }
            }
            return totalCount;
    	}
}
