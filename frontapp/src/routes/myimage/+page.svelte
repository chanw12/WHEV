<script lang="ts">
	import { page } from '$app/stores';
	import rq from '$lib/rq/rq.svelte';
	import { get } from 'svelte/store';
	import type { components } from '$lib/types/api/v1/schema';

	async function load() {
		if (import.meta.env.SSR) throw new Error('CSR ONLY');
		const { data, error } = await rq.apiEndPoints().GET(`/api/v1/image/{memberUUID}`, {
			params: {
				path: {
					memberUUID: rq.member.uuid
				}
			}
		});
		console.log(data?.data.items?.content);
		return data?.data.items?.content;
	}
</script>

<!-- 탭 추가 -->
<div class="max-w-4xl mx-auto mb-4">
	{#await load()}
		<p class="text-center">loading...</p>
	{:then images}
		<div class="mt-32">
			{#each images as image}
				<li class="flex my-5 items-center bg-white p-4 rounded-lg shadow-md justify-between">
					<img
						src={import.meta.env.VITE_CORE_API_BASE_URL + '/gen' + image.path}
						class="w-24 h-24 rounded-lg object-cover mr-4"
						alt=""
					/>
					<div class="flex-grow">
						<p class="text-gray-700">좋아요: {image.voters.length}</p>
						<p class="text-gray-700">댓글: {image.comments_len}</p>
						<p class="text-gray-700">구매횟수: {image.purchase_count}</p>
					</div>
					<div class="flex space-x-2">
						<button class="px-4 py-2 bg-blue-500 text-white rounded-lg">수정</button>
						<button class="px-4 py-2 bg-red-500 text-white rounded-lg">삭제</button>
					</div>
				</li>
			{/each}
		</div>
	{:catch error}
		<p class="text-center">Error: {error.message}</p>
	{/await}
</div>
