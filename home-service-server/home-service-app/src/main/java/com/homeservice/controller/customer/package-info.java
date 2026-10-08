/**
 * 业务练习入口：Controller 只接收 DTO/Query、调用 IXxxService 并返回 VO。
 * 事务、资格、状态、幂等和资源归属由未来 Service 实现；本阶段不提供假成功业务接口。
 * 地址先锁客户账号行；SKU 删除须同事务解绑入口；成功分配每单最多一条；通知在事务提交后发送。
 */
package com.homeservice.controller.customer;
