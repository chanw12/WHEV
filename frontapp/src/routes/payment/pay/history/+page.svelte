<!-- src/routes/points.svelte -->
<script>
	import { onMount } from 'svelte';
	import { loadTossPayments } from '@tosspayments/payment-sdk';
	import rq from '$lib/rq/rq.svelte';
	import { goto } from '$app/navigation';
	import { isCancel } from 'axios';

	let cache = $state(100);

	let historyList = $state([]);
	onMount(async () => {
		const params = new URLSearchParams(window.location.search);
		const data = await rq.apiEndPoints().GET(`/api/v1/payment/history`, {
			params: {
				query: {
					page: 0
				}
			}
		});
		cache = rq.member.cache;
		historyList = data.data?.data.content;
		console.log(historyList);
	});
</script>

<div class=" flex items-center justify-center">
	<div class="p-4 mt-20 w-3/4 flex-row">
		<div class="bg-blue-100 p-4 rounded-lg">
			<div class="flex justify-between items-center">
				<div class="text-lg font-bold">포인트</div>
				<div class="text-2xl font-bold">{cache}원</div>
			</div>
			<a href="/payment" class="btn btn-primary mt-2">포인트 충전하기</a>
		</div>

		<div class="mt-6">
			<div class="text-lg font-bold">충전 내역</div>
			{#each historyList as history}
				<div class="flex justify-between items-center border-b py-2">
					<div class="w-1/4">
						{new Date(history.createdAt).toLocaleString('ko-KR', {
							year: 'numeric',
							month: '2-digit',
							day: '2-digit',
							hour: '2-digit',
							minute: '2-digit'
						})}
					</div>
					<div
						class="w-1/4 text-center {history.cancelYN === true
							? 'text-red-500'
							: history.paySuccessYN === true
								? 'text-blue-500'
								: 'text-gray-500'}"
					>
						{history.cancelYN === true
							? '결제 취소'
							: history.paySuccessYN === true
								? '성공'
								: '실패'}
					</div>
					{#if !history.cancelYN && history.paySuccessYN === true && new Date() - new Date(history.createdAt) < 24 * 60 * 60 * 1000}
						<div class="w-1/4 text-center">
							<button
								on:click={() => {
									rq.goTo(
										import.meta.env.VITE_CORE_FRONT_BASE_URL +
											'/payment/pay/cancel?id=' +
											history.paymentHistoryId
									);
								}}
								class="btn btn-error bg-red-300 mt-2">결제 취소</button
							>
						</div>
					{:else}
						<div class="w-1/4 text-center"></div>
					{/if}
					<div class="w-1/3 text-right">{history.amount.toLocaleString()}원</div>
				</div>
			{/each}
		</div>
	</div>
</div>
