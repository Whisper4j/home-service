<script setup lang="ts">
import { reactive, ref } from 'vue'
import { request, useMock } from '../../api/client'
import { allPages } from '../../api/pagination'
import type { Schema } from '../../api/types'
import { dateOf } from '../../utils/format'
import { label } from '../../utils/labels'
import { useTask } from '../../composables/useTask'
import { useRefresh } from '../../composables/useRefresh'
import Feedback from '../../components/Feedback.vue'
import Pagination from '../../components/Pagination.vue'
const page = ref<Schema['WorkerPageDTO']>({ list: [], total: 0, pages: 0 }), skills = ref<Schema['SkillVO'][]>([]), slots = ref<Schema['SlotVO'][]>([])
const query = reactive({ pageNo: 1, pageSize: 20, keyword: '', skillId: '', dispatchEnabled: '' })
const form = reactive<Schema['WorkerCreateDTO']>({ username: '', password: '', displayName: '', phone: '', cityCode: '440100', skillIds: [], dispatchEnabled: true })
const editing = ref(''), open = ref(false), selectedWorker = ref(''), slotDate = ref(dateOf(Date.now()))
const { busy, error, success, run } = useTask()
async function refreshData() { const [p, s] = await Promise.all([request('listWorkers', { query: { pageNo: query.pageNo, pageSize: query.pageSize, ...(query.keyword ? { keyword: query.keyword } : {}), ...(query.skillId ? { skillId: query.skillId } : {}), ...(query.dispatchEnabled ? { dispatchEnabled: query.dispatchEnabled === 'true' } : {}) } }), allPages(n => request('listAdminSkill', { query: { pageNo: n, pageSize: 100 } }))]); page.value = p; skills.value = s }
function load() { return run(refreshData) }
function search() { query.pageNo = 1; void load() }
function edit(worker?: Schema['WorkerVO']) { editing.value = worker?.id || ''; open.value = true; Object.assign(form, { username: worker?.username || '', password: '', displayName: worker?.displayName || '', phone: worker?.phone || '', cityCode: '440100', skillIds: [...worker?.skillIds || []], dispatchEnabled: worker?.dispatchEnabled ?? true }) }
function save() { void run(async key => { const { username, password, ...rest } = form; const dto = { ...rest, skillIds: [...rest.skillIds] }; if (editing.value) await request('updateWorker', { id: editing.value, body: dto, idempotencyKey: key }); else await request('createWorker', { body: { ...dto, username, password }, idempotencyKey: key }); open.value = false; await refreshData() }, '人员资料已保存', JSON.stringify({ ...form, editing: editing.value })) }
function loadSlots() { void run(async () => { slots.value = await request('getAdminWorkerSlots', { id: selectedWorker.value, query: { date: slotDate.value } }) }) }
async function inspect(id: string) { selectedWorker.value = id; if (useMock) slotDate.value = dateOf((await import('../../mock/transport')).mockNow()); loadSlots() }
useRefresh(load)
</script>
<template><h1>服务人员</h1><form class="filters" @submit.prevent="search"><label>姓名 / 用户名<input v-model.trim="query.keyword" /></label><label>技能<select v-model="query.skillId"><option value="">全部</option><option v-for="skill in skills" :key="skill.id" :value="skill.id">{{ skill.name }}</option></select></label><label>允许派单<select v-model="query.dispatchEnabled"><option value="">全部</option><option value="true">允许</option><option value="false">暂停</option></select></label><button :disabled="busy">查询</button><button type="button" @click="edit()">创建服务人员</button></form><Feedback :error="error" :success="success" :busy="busy" />
  <section v-if="open" class="panel"><h2>{{ editing ? '编辑人员' : '创建人员账号' }}</h2><form @submit.prevent="save"><div class="form-grid"><label v-if="!editing">用户名<input v-model.trim="form.username" required pattern="[A-Za-z][A-Za-z0-9_]{2,31}" /></label><label v-if="!editing">初始密码<input v-model="form.password" type="password" required minlength="8" maxlength="72" autocomplete="new-password" /></label><label>姓名<input v-model.trim="form.displayName" required maxlength="40" /></label><label>联系电话<input v-model.trim="form.phone" required pattern="1[0-9]{10}" /></label><label>服务城市<select v-model="form.cityCode"><option value="440100">广州市</option></select></label></div><div class="check-group"><label v-for="skill in skills" :key="skill.id" class="inline"><input v-model="form.skillIds" type="checkbox" :value="skill.id" />{{ skill.name }}</label></div><label class="inline"><input v-model="form.dispatchEnabled" type="checkbox" />允许新分配（不改变已有订单）</label><p>新人员需登录后首次设置排班，才具备可接单时间槽。</p><div class="actions"><button :disabled="busy">保存人员</button><button type="button" @click="open = false">关闭</button></div></form></section>
  <div class="table-scroll"><table><thead><tr><th>人员</th><th>技能</th><th>账号 / 允许派单</th><th>操作</th></tr></thead><tbody><tr v-for="worker in page.list" :key="worker.id"><td>{{ worker.displayName }} / {{ worker.username }}<br />{{ worker.phone }}</td><td>{{ worker.skillIds.map(id => skills.find(s => s.id === id)?.name || id).join('、') }}</td><td>{{ label(worker.status) }} / {{ worker.dispatchEnabled ? '允许' : '暂停' }}</td><td><button @click="edit(worker)">编辑</button> <button @click="inspect(worker.id)">查看时间槽</button></td></tr></tbody></table></div><Pagination v-bind="query" :total="page.total" :pages="page.pages" @change="query.pageNo = $event; load()" @resize="query.pageSize = $event; search()" />
  <section v-if="selectedWorker" class="panel"><h2>人员 {{ selectedWorker }} 时间槽</h2><form class="filters" @submit.prevent="loadSlots"><label>日期<input v-model="slotDate" type="date" required /></label><button :disabled="busy">查询</button></form><div class="slots"><div v-for="slot in slots" :key="slot.startTime" class="slot" :class="{ occupied: slot.status !== 'AVAILABLE' }">{{ slot.startTime.slice(11, 16) }}—{{ slot.endTime.slice(11, 16) }}<br />{{ label(slot.status) }} {{ slot.bookingType ? label(slot.bookingType) : '' }}<br /><RouterLink v-if="slot.orderId" :to="`/admin/orders/${slot.orderId}`">订单 {{ slot.orderId }}</RouterLink></div></div></section>
</template>
