<script setup lang="ts">
import { reactive, ref } from 'vue'
import { request } from '../../api/client'
import type { Schema } from '../../api/types'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import { label } from '../../utils/labels'
import Feedback from '../../components/Feedback.vue'
import Pagination from '../../components/Pagination.vue'
const query = reactive({ pageNo: 1, pageSize: 20, keyword: '', role: '' as Schema['Role'] | '', status: '' as Schema['AccountStatus'] | '' })
const page = ref<Schema['AccountPageDTO']>({ list: [], total: 0, pages: 0 }), editing = ref('')
const form = reactive({ displayName: '', phone: '' }), { busy, error, success, run } = useTask()
async function refreshData() { page.value = await request('listAccounts', { query: { pageNo: query.pageNo, pageSize: query.pageSize, ...(query.keyword ? { keyword: query.keyword } : {}), ...(query.role ? { role: query.role } : {}), ...(query.status ? { status: query.status } : {}) } }) }
function load() { return run(refreshData) }
function search() { query.pageNo = 1; void load() }
function toggle(account: Schema['AccountVO']) { void run(async key => { await request('setAccountStatus', { id: account.id, body: { status: account.status === 'ENABLED' ? 'DISABLED' : 'ENABLED' }, idempotencyKey: key }); await refreshData() }, '账号状态已更新', `${account.id}:${account.status}`) }
function save() { void run(async key => { await request('updateAccountProfile', { id: editing.value, body: { ...form }, idempotencyKey: key }); editing.value = ''; await refreshData() }, '账号资料已保存', JSON.stringify(form)) }
useRefresh(load)
</script>
<template><h1>账号管理</h1><p>客户自行注册，服务人员由管理员创建；当前只有初始化超级管理员。</p><form class="filters" @submit.prevent="search"><label>用户名 / 姓名<input v-model.trim="query.keyword" /></label><label>角色<select v-model="query.role"><option value="">全部</option><option value="CUSTOMER">客户</option><option value="WORKER">服务人员</option><option value="ADMIN">管理员</option></select></label><label>状态<select v-model="query.status"><option value="">全部</option><option value="ENABLED">启用</option><option value="DISABLED">禁用</option></select></label><button :disabled="busy">查询</button></form><Feedback :error="error" :success="success" :busy="busy" /><form v-if="editing" class="panel" @submit.prevent="save"><h2>编辑账号 {{ editing }}</h2><label>姓名<input v-model.trim="form.displayName" required maxlength="40" /></label><label>联系电话<input v-model.trim="form.phone" required pattern="1[0-9]{10}" /></label><div class="actions"><button :disabled="busy">保存资料</button><button type="button" @click="editing = ''">取消编辑</button></div></form><div class="table-scroll"><table><thead><tr><th>用户名</th><th>姓名 / 联系方式</th><th>角色</th><th>状态</th><th>操作</th></tr></thead><tbody><tr v-for="account in page.list" :key="account.id"><td>{{ account.username }}</td><td>{{ account.displayName }}<br />{{ account.phone }}</td><td>{{ label(account.role) }}</td><td>{{ label(account.status) }}</td><td><button :disabled="busy || account.role === 'ADMIN'" @click="toggle(account)">{{ account.status === 'ENABLED' ? '禁用' : '启用' }}</button> <button @click="editing = account.id; form.displayName = account.displayName; form.phone = account.phone">编辑资料</button></td></tr></tbody></table></div><p v-if="!busy && !page.list.length" class="empty">暂无符合条件的账号。</p><Pagination v-bind="query" :total="page.total" :pages="page.pages" @change="query.pageNo = $event; load()" @resize="query.pageSize = $event; search()" /></template>
