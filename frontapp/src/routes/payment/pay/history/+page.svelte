<!-- src/routes/points.svelte -->
<script>
	import { onMount } from 'svelte';
	import { loadTossPayments } from '@tosspayments/payment-sdk';
	import rq from '$lib/rq/rq.svelte';

	let points = 19800;
	let transactions = [
		{ date: '2023-06-12 15:25', status: '성공', amount: 50000 },
		{ date: '2023-02-15 15:35', status: '실패', amount: 300000 },
		{ date: '2023-02-03 11:14', status: '실패', amount: 20000000 },
		{ date: '2023-02-03 08:41', status: '실패', amount: 10000 }
	];
	let orderId;
	let amount;
	let paymentKey;
	let paymentdata = 0;
	onMount(async () => {
		const params = new URLSearchParams(window.location.search);
		const data = await rq.apiEndPoints().GET(`/api/v1/payment/history`, {
			params: {
				query: {
					page: 0
				}
			}
		});
		console.log(data);
	});
</script>

<div class=" flex items-center justify-center">
	<div class="p-4 mt-20 w-3/4 flex-row">
		<div class="bg-blue-100 p-4 rounded-lg">
			<div class="flex justify-between items-center">
				<div class="text-lg font-bold">포인트</div>
				<div class="text-2xl font-bold">{points.toLocaleString()}원</div>
			</div>
			<button class="btn btn-primary mt-2">포인트 충전하기</button>
		</div>

		<div class="mt-6">
			<div class="text-lg font-bold">충전 내역</div>
			{#each transactions as transaction}
				<div class="flex justify-between items-center border-b py-2">
					<div class="w-1/3">{transaction.date}</div>
					<div
						class="w-1/3 text-center {transaction.status === '성공'
							? 'text-blue-500'
							: 'text-gray-500'}"
					>
						{transaction.status}
					</div>
					<div class="w-1/3 text-right">{transaction.amount.toLocaleString()}원</div>
				</div>
			{/each}
		</div>
	</div>
</div>
