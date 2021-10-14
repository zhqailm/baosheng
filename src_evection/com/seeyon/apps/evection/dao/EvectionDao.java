package com.seeyon.apps.evection.dao;

import com.seeyon.ctp.util.JDBCAgent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EvectionDao {
    public String getMemberNameByMemberId (String memberId)  {
        Connection con = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        String memberName = "";
        try {
            con = JDBCAgent.getRawConnection();
            pt = con.prepareStatement("select * from ORG_MEMBER where id = ?)");
            pt.setString(1,memberId);
            rs = pt.executeQuery();
            if(rs.next()){
                memberName = rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
        	if (rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pt != null){
                try {
                    pt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
		}
        return memberName;
    }
    public String getIdCardByMemberId (String memberId)  {
        Connection con = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        String idCard = "";
        try {
            con = JDBCAgent.getRawConnection();
            pt = con.prepareStatement("select * from addressbook where member_id = ?");
            pt.setString(1,memberId);
            rs = pt.executeQuery();
            if (rs.next()){
                idCard = rs.getString("ext_attr_1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
        	if (rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pt != null){
                try {
                    pt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
		}
        return idCard;
    }
}
