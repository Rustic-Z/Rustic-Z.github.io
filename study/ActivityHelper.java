package so.dian.leo.common.util;

import com.alibaba.fastjson.JSON;
import com.meidalife.common.exception.BizException;
import org.activiti.engine.*;
import org.activiti.engine.history.*;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.entity.HistoricFormPropertyEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.activiti.engine.task.TaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import so.dian.leo.exception.LeoExcs;

import java.util.*;

/**
 * activiti核心交互类
 * Created by damao on 2017/1/14.
 */
@Component
public class ActivitiHelper {

    private static Logger logger = LoggerFactory.getLogger(ActivitiHelper.class);

    //查询组任务
    public static final String TASK_ROLE_GROUP = "group";
    //查询用户任务
    public static final String TASK_ROLE_USER = "user";
    //流程发起人,调用 startWorkflow 时，在 variables 中传入
    public static final String START_USER = "startUser";
    //下一环节流程候选处理人id, 在 variables 中传入
    public static final String NEXT_CONSUME_USER = "nextUser";
    //下一环节流程候选处理组id, 在 variables 中传入
    public static final String NEXT_CONSUME_GROUP = "nextGroup";
    //下一环节流程指派人, 在 variables 中传入
    public static final String NEXT_CONSUME_ASSIGNEE = "assignee";
    //环节描述
    public static final String NEXT_DESCRIPTION = "description";
    //每个任务记录一个工单环节id
    public static final String WORK_STEP_ID = "stepId";
    //下一个并发子流程的业务key
    public static final String SUB_PROCESS_KEYS = "subProcessKeys";
    public static final String SUB_PROCESS_KEY = "subProcessKey";
    //对应的流程图类型
    public static final String PROCESS_TYPE_ALARM = "sodian-alarm";
    public static final String PROCESS_TYPE_REPAIR = "sodian-repair";
    /**
     * 当下一环节路径选择需要条件判断的时候需要, 在 variables 中传入
     * ********小电维修流程*********
     * **客服电话拜访结果
     * condition:商家已改进,result=success
     * condition:设备损坏需维修,result=repair
     * condition:设备回收,result=getback
     * condition:转风控,result=check
     * **维修结果
     * condition:完成维修,result=success
     * condition:门店关闭无法维修,result=fail
     * **bd现场教学
     * condition:完成现场教学,result=success
     * condition:设备损坏需维修,result=repair
     * condition:门店无法配合,result=fail
     * **设备回收
     * condition:完成回收,result=success
     * condition:门店关闭,result=fail
     */
    public static final String FLOW_CONDITION_PARAM = "result";

    private static ProcessEngineConfiguration processEngineConfiguration;
    private static RepositoryService repositoryService;
    private static RuntimeService runtimeService;
    private static TaskService taskService;
    private static HistoryService historyService;
    private static FormService formService;

    static {
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("leo-config.xml") ;
            if (processEngineConfiguration == null) {
                processEngineConfiguration =
                        (ProcessEngineConfiguration) context.getBean("processEngineConfiguration");
            }
            if (repositoryService == null) {
                repositoryService = (RepositoryService) context.getBean("repositoryService");
            }
            if (runtimeService == null) {
                runtimeService = (RuntimeService) context.getBean("runtimeService");
            }
            if (taskService == null) {
                taskService = (TaskService) context.getBean("taskService");
            }
            if (historyService == null) {
                historyService = (HistoryService) context.getBean("historyService");
            }
            if (formService == null) {
                formService = (FormService) context.getBean("formService");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入activiti表到数据库中
     */
    public static void initActiviti() {
        ProcessEngine processEngine = processEngineConfiguration
                .setDatabaseSchemaUpdate(ProcessEngineConfigurationImpl.DB_SCHEMA_UPDATE_CREATE)
                .buildProcessEngine();
        logger.info(processEngine == null ?
                "初始化activiti引擎失败！"
                :
                "初始化activiti引擎成功，processEngine's name :" + processEngine.getName());
    }

    /**
     * 部署activiti流程
     * @param bpmPath 流程图的路径
     * @return 是否成功
     */
    public static boolean deployBpm(String bpmPath) {
        if (!StringUtil.isNotBlank(bpmPath)) {
            return false;
        }
        Deployment deployment = repositoryService
                .createDeployment()
                .addClasspathResource(bpmPath)
                .deploy();
        logger.info(deployment == null ?
                "部署流程失败！"
                :
                "新部署有一个流程，部署ID：" + deployment.getId()
                + "，部署name：" + deployment.getName()
                + "，部署时间：" + deployment.getDeploymentTime()
                + "，部署tenantId：" + deployment.getTenantId());
        return true;
    }

    /**
     * 删除已部署的某个流程
     * @param deploymentId 部署id
     * @return 是否成功
     */
    public static boolean deleteDeployment(String deploymentId) {
        if (!StringUtil.isNotBlank(deploymentId)) {
            return false;
        }
        repositoryService.deleteDeployment(deploymentId, true);
        logger.info("删除一个已发布的流程！");
        return true;
    }

    /**
     * 启动一个流程
     * @param processKey 标记流程的唯一key
     * @param businessKey 将要与该流程实例绑定的业务id
     * @param variables 显示的指明流程图中接下来任务节点的参数值,没有则直接new一个map做为参数即可
     * @return 流程实例id
     */
    public static String startWorkflow(String processKey, String businessKey, Map<String, Object> variables) {
        if (!StringUtil.isNotBlank(processKey) || !StringUtil.isNotBlank(businessKey) || variables == null) {
            return null;
        }
        if (!StringUtil.isNotBlank((String) variables.get(NEXT_CONSUME_GROUP))) {
            variables.put(NEXT_CONSUME_GROUP, "0");  //没有指定下一环节处理组的话，自动补全为0
        }
        if (!StringUtil.isNotBlank((String) variables.get(NEXT_CONSUME_USER))) {
            variables.put(NEXT_CONSUME_USER, "0");  //没有指定下一环节处理人的话，自动补全为0
        }
        if (!StringUtil.isNotBlank((String) variables.get(NEXT_CONSUME_ASSIGNEE))) {
            variables.put(NEXT_CONSUME_ASSIGNEE, null);  //没有指定下一环节处理人的话，自动补全为null
        }
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey, businessKey, variables);
        //填充用户任务表单信息
        if (StringUtil.isNotBlank((String) variables.get(ActivitiHelper.WORK_STEP_ID))) {
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
            Map<String, String> formProperties = new HashMap<>();
            formProperties.put(ActivitiHelper.WORK_STEP_ID, (String) variables.get(ActivitiHelper.WORK_STEP_ID));
            formService.saveFormData(task.getId(), formProperties);
        }
        logger.info(processInstance == null ?
                "启动一个流程失败, processKey:" + processKey + ", businesskey:" + businessKey
                        + ", variables:" + JSON.toJSONString(variables)
                :
                "启动一个流程成功, processKey:" + processKey + ", businesskey:" + businessKey
                        + ", version:" + processInstance.getProcessDefinitionVersion());
        if (processInstance == null) {
            throw new BizException(LeoExcs.ACTIVITI_START_ERROR);
        }
        return processInstance.getId();
    }

    /**
     * 将任务list转换为Map<workflowId, taskId>
     * @param tasks
     * @param <T>
     * @return
     */
    public static <T extends TaskInfo> void convertTaskToWorkflowMap(List<T> tasks, Map<String, String> taskMap) {
        ProcessInstance instance;
        HistoricProcessInstance historicInstance;
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
        for (T task : tasks) {
            if (!StringUtil.isNotBlank(task.getProcessInstanceId())) {
                continue;
            }
            //有的任务可能所处的流程已经结束了，那么runtimeService查找不出流程实例
            instance = processInstanceQuery.processInstanceId(task.getProcessInstanceId()).singleResult();
            if (instance != null) {
                taskMap.put(instance.getBusinessKey(), task.getId());
                continue;
            }
            historicInstance = historicProcessInstanceQuery.processInstanceId(task.getProcessInstanceId()).singleResult();
            if (historicInstance != null) {
                taskMap.put(historicInstance.getBusinessKey(), task.getId());
            }
        }
    }

    /**
     * 将任务list转换为Map<workstepId, taskId>
     * @param tasks
     * @param <T>
     * @return
     */
    public static <T extends TaskInfo> void convertTaskToWorkstepMap(List<T> tasks, Map<String, String> taskMap) {
        List<HistoricDetail> properties;
        for (T task : tasks) {
            properties = historyService.createHistoricDetailQuery().taskId(task.getId()).formProperties().list();
            if (properties.isEmpty()) {
                continue;
            }
            for (HistoricDetail property : properties) {
                if (!(property instanceof HistoricFormPropertyEntity)) {
                    continue;
                }
                HistoricFormPropertyEntity formEntity = (HistoricFormPropertyEntity) property;
                if (ActivitiHelper.WORK_STEP_ID.equals(formEntity.getPropertyId())) {
                    taskMap.put(formEntity.getPropertyValue(), task.getId());
                    break;
                }
            }
        }
    }

    /**
     * 获取正在运行的任务
     * @param processKey
     * @param isClaim true 查询被指派的任务，false 不查询已经指派的任务
     * @param roleLevel group or user
     * @param roleId groupId or userId
     * @param businessKey 对应的是workflow表的no
     * @param description 检索关键词
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param firstResult 第一条数据
     * @param maxResults 最大查询数
     * @return
     */
    public static long getRuntimeTasks(String processKey, boolean isClaim,
                                                      String roleLevel, Integer roleId,
                                                      String businessKey, String description,
                                                      Date startTime, Date endTime,
                                                      Integer firstResult, Integer maxResults,
                                                      Map<String, String> taskMap) {
        TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey(processKey).orderByTaskId().desc();
        if (!isClaim) {
            taskQuery.taskUnassigned();
        }
        if (ActivitiHelper.TASK_ROLE_GROUP.equals(roleLevel)) {
            taskQuery = taskQuery.taskCandidateGroup(String.valueOf(roleId));
        } else if (ActivitiHelper.TASK_ROLE_USER.equals(roleLevel)) {
            taskQuery = taskQuery.taskAssignee(String.valueOf(roleId));
        }
        if (StringUtil.isNotBlank(businessKey)) {
            taskQuery = taskQuery.processInstanceBusinessKey(businessKey);
        }
        if (StringUtil.isNotBlank(description)) {
            taskQuery = taskQuery.taskDescriptionLikeIgnoreCase("%" + description + "%");
        }
        if (startTime != null && endTime != null) {
            taskQuery = taskQuery.taskCreatedAfter(startTime)
                    .taskCreatedBefore(endTime);
        }
        if (firstResult == null || maxResults == null) {
            convertTaskToWorkflowMap(taskQuery.list(), taskMap);
        } else {
            convertTaskToWorkflowMap(taskQuery.listPage(firstResult, maxResults), taskMap);
        }
        return taskQuery.count();
    }

    /**
     * 获取运行时任务的统计值
     * @param processKey
     * @param isClaim
     * @param roleLevel
     * @param roleId
     * @return
     */
    public static long getRuntimeTaskCount(String processKey, boolean isClaim, String roleLevel, Integer roleId) {
        TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey(processKey);
        if (!isClaim) {
            taskQuery.taskUnassigned();
        }
        if (ActivitiHelper.TASK_ROLE_GROUP.equals(roleLevel)) {
            taskQuery = taskQuery.taskCandidateGroup(roleId.toString());
        } else if (ActivitiHelper.TASK_ROLE_USER.equals(roleLevel)) {
            taskQuery = taskQuery.taskAssignee(roleId.toString());
        }
        long count = taskQuery.count();
        return count;
    }

    /**
     * 获取历史任务
     * @param processKey
     * @param roleLevel
     * @param roleId
     * @param businesskey
     * @param description
     * @param startTime
     * @param endTime
     * @param firstResult
     * @param maxResults
     * @return
     */
    public static long getHistoryTasks(String processKey, String roleLevel, String roleId,
                                                      String businesskey, String description,
                                                      Date startTime, Date endTime,
                                                      Integer firstResult, Integer maxResults,
                                                      Map<String, String> taskMap) {
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService
                .createHistoricTaskInstanceQuery()
                .processDefinitionKey(processKey).orderByTaskId().desc();
        if (ActivitiHelper.TASK_ROLE_GROUP.equals(roleLevel)) {
            historicTaskInstanceQuery = historicTaskInstanceQuery.finished().taskCandidateGroup(roleId);
        } else if (ActivitiHelper.TASK_ROLE_USER.equals(roleLevel)) {
            historicTaskInstanceQuery = historicTaskInstanceQuery.finished().taskAssignee(roleId);
        }
        if (StringUtil.isNotBlank(businesskey)) {
            historicTaskInstanceQuery = historicTaskInstanceQuery.processInstanceBusinessKey(businesskey);
        }
        if (startTime != null && endTime != null) {
            historicTaskInstanceQuery = historicTaskInstanceQuery
                    .taskCompletedAfter(startTime)
                    .taskCompletedBefore(endTime);
        }
        if (StringUtil.isNotBlank(description)) {
            historicTaskInstanceQuery = historicTaskInstanceQuery.taskDescriptionLike(description);
        }
        if (firstResult == null || maxResults == null) {
            convertTaskToWorkstepMap(historicTaskInstanceQuery.list(), taskMap);
        } else {
            convertTaskToWorkstepMap(historicTaskInstanceQuery.listPage(firstResult, maxResults), taskMap);
        }
        return historicTaskInstanceQuery.count();
    }

    public static void getHistoryTasksTest(Map<String, String> taskMap) {
        List<HistoricTaskInstance> list = historyService
                .createHistoricTaskInstanceQuery().taskAssignee("5815").finished().list();
        convertTaskToWorkflowMap(list, taskMap);
    }

    /**
     * 获取历史任务的统计值
     * @param processKey
     * @param roleLevel
     * @param roleId
     * @return
     */
    public static long getHistoryTaskCount(String processKey, String roleLevel, String roleId) {
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService
                .createHistoricTaskInstanceQuery()
                .processDefinitionKey(processKey);
        if (ActivitiHelper.TASK_ROLE_GROUP.equals(roleLevel)) {
            historicTaskInstanceQuery = historicTaskInstanceQuery.finished().taskCandidateGroup(roleId);
        } else if (ActivitiHelper.TASK_ROLE_USER.equals(roleLevel)) {
            historicTaskInstanceQuery = historicTaskInstanceQuery.finished().taskAssignee(roleId);
        }
        return historicTaskInstanceQuery.count();
    }

    /**
     * 认领一个任务
     * 暂时不需要此方法，如果需要使用官方提供的任务绑定api，
     * 需要用户关系信息存放在activiti的表格中
     * @param userId 用户id
     * @param taskId 任务id
     * @return
     */
    public static boolean claimTask(String userId, String taskId) {
        if (!StringUtil.isNotBlank(userId) || !StringUtil.isNotBlank(taskId)) {
            return false;
        }
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return false;
        }
        taskService.claim(taskId, userId);
        return true;
    }

    /**
     * 完成一个任务
     * @param consumerId
     * @param taskId
     * @param variables
     * @return
     */
    public static boolean completedTask(String consumerId, String taskId, Map<String, Object> variables) {
        if (!StringUtil.isNotBlank(consumerId) || !StringUtil.isNotBlank(taskId) || variables == null) {
            return false;
        }
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return false;
        }
        taskService.complete(taskId, variables);
        return true;
    }

    /**
     * 调起外部子流程
     * @deprecated 代码已封印，原有待维修流程走自己的业务逻辑
     * @param consumerId
     * @param taskId
     * @param variables
     * @return
     */
    public static Map<String, String> callSubActiviti(String consumerId, String taskId,
                                         Map<String, String> businessMap, Map<String, Object> variables) {
        if (!StringUtil.isNotBlank(consumerId)
                || !StringUtil.isNotBlank(taskId)
                || businessMap == null
                || variables == null) {
            return null;
        }
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            return null;
        }
        variables.put(ActivitiHelper.START_USER, consumerId);
        variables.put(ActivitiHelper.NEXT_DESCRIPTION, task.getDescription());
        taskService.complete(taskId, variables);
        ProcessInstance superProcess = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult();
        if (superProcess == null) {
            throw new BizException(LeoExcs.ACTIVITI_SUBPROCESS_START_ERROR);
        }

        List<ProcessInstance> subProcesses = runtimeService.createProcessInstanceQuery()
                .superProcessInstanceId(superProcess.getProcessInstanceId())
                .orderByProcessInstanceId()
                .desc()
                .list();
        if (subProcesses.isEmpty()) {
            throw new BizException(LeoExcs.ACTIVITI_SUBPROCESS_START_ERROR);
        }

        Iterator<Map.Entry<String, String>> iterator = businessMap.entrySet().iterator();
        Map.Entry<String, String> entry;
        int i = 0;
        ProcessInstance subProcess;
        Map<String, String> formProperties;
        Map<String, String> processIdMap = new HashMap<>();
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (i >= subProcesses.size()) {
                break;
            }
            subProcess = subProcesses.get(i++);
            runtimeService.updateBusinessKey(subProcess.getProcessInstanceId(), entry.getKey());
            formProperties = new HashMap<>();
            task = taskService.createTaskQuery().processInstanceId(subProcess.getId()).singleResult();
            formProperties.put(ActivitiHelper.WORK_STEP_ID, entry.getValue());
            formService.saveFormData(task.getId(), formProperties);
            processIdMap.put(entry.getKey(), subProcess.getProcessInstanceId());
        }
        return processIdMap;
    }

    /**
     * 获取某个流程实例的最后一个任务id
     * @param processKey
     * @param businessKey
     * @return
     */
    public static String getLastTaskIdByProcessId(String processKey, String businessKey) {
        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionKey(processKey)
                .processInstanceBusinessKey(businessKey)
                .orderByTaskId().desc().list();
        if (tasks == null || tasks.isEmpty()) {
            return null;
        }
        return tasks.get(0).getId();
    }

}
