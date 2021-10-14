package com.seeyon.apps.evection.event;



import com.seeyon.apps.evection.manager.EvectionManager;
import com.seeyon.apps.evection.util.AppendString;
import com.seeyon.apps.evection.util.Connections;
import com.seeyon.apps.evection.util.DateToString;
import com.seeyon.ctp.common.constants.ApplicationCategoryEnum;
import com.seeyon.ctp.workflow.event.AbstractWorkflowEvent;
import com.seeyon.ctp.workflow.event.WorkflowEventAdvancePageInfo;
import com.seeyon.ctp.workflow.event.WorkflowEventData;
import com.seeyon.ctp.workflow.event.WorkflowEventResult;
import com.seeyon.ctp.workflow.util.WorkflowEventConstants.WorkflowEventType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import redis.clients.jedis.Connection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class EvectionWorkFlowEvent extends AbstractWorkflowEvent {

     private Log logger = LogFactory.getLog(EvectionWorkFlowEvent.class);

     private EvectionManager evectionManager;
     
     public EvectionManager getEvectionManager() {
 		return evectionManager;
 	}

 	public void setEvectionManager(EvectionManager evectionManager) {
 		this.evectionManager = evectionManager;
 	}

    @Override
    public String getId() {
        return "20210817";
    }


	@Override
    public String getLabel() {
        return "员工出差申请事件";
    }
    
    @Override
	public ApplicationCategoryEnum getAppName() {
//		return super.getAppName();
		return ApplicationCategoryEnum.form;
	}
    
    @Override
	public WorkflowEventAdvancePageInfo getOpenPageInfo() {
		return super.getOpenPageInfo();
	}

	@Override
	public WorkflowEventType getType() {
		return super.getType();
	}

	//结束前事件
    public WorkflowEventResult onBeforeFinishWorkitem(WorkflowEventData data) {
        WorkflowEventResult wr = new WorkflowEventResult();
        String proposer = (String) data.getBusinessData().get("申请人");
        Date begin = (Date) data.getBusinessData().get("开始日期时间");
        Date end = (Date) data.getBusinessData().get("结束日期时间");

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String tempBeginTime = simpleDateFormat.format(begin);
        String tempEndTime = simpleDateFormat.format(end);

        String startTime = DateToString.toDateString(tempBeginTime);
        String endTime = DateToString.toDateString(tempEndTime);

        String certNum = evectionManager.getIdCardByMemberId(proposer);

        String personId = Connections.getPersonId(certNum);

        if (personId == null) {
            logger.info("=====出差申请出错，身份证信息不匹配，查询失败=====");
            wr.setAlertMessage("身份证信息不匹配，出差申请失败，请核对身份证信息");
            return wr;
        }

        String evectionJson = AppendString.evectionJson(personId,certNum,startTime,endTime);

        String result = Connections.getAttendanceResult(evectionJson);

        JSONObject js = new JSONObject(result);

        String code = js.getString("code");
        String msg = js.getString("msg");

        if(code == "0xa8520306"){
            logger.info("=====时间内同类特殊出勤数据已存在，申请出差失败=====");
            wr.setAlertMessage(msg);
            return wr;
        }


        if (!(code.equals("0") && msg.equals("success"))){
            logger.info("=====出差参数信息有误，申请出差失败=====");
            wr.setAlertMessage(msg);
        }

        return wr;
    }




    @Override
    public void onProcessFinished(WorkflowEventData data) {

    }

}
