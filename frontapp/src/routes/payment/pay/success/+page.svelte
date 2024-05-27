<script>
	import { onMount } from 'svelte';
	import { loadTossPayments } from '@tosspayments/payment-sdk';
	import rq from '$lib/rq/rq.svelte';

	let orderId;
	let amount;
	let paymentKey;
	let paymentdata = 0;
	onMount(async () => {
		const params = new URLSearchParams(window.location.search);
		paymentKey = params.get('paymentKey');
		orderId = params.get('orderId');
		amount = params.get('amount');

		const data = await rq.apiEndPoints().GET(`/api/v1/payment/toss/success`, {
			params: {
				query: {
					paymentKey,
					orderId,
					amount
				}
			}
		});
		paymentdata = data.data?.data;
	});
</script>

<div class="flex flex-col items-center justify-center min-h-screen bg-gray-100 p-4">
	<div class="bg-white shadow-md rounded-lg p-8 max-w-md w-full">
		<div class="flex flex-col items-center mb-4">
			<svg
				class="w-16 h-16 text-blue-500 mb-4"
				fill="none"
				stroke="currentColor"
				viewBox="0 0 24 24"
				xmlns="http://www.w3.org/2000/svg"
			>
				<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"
				></path>
			</svg>
			<h2 class="text-2xl font-bold mb-2">포인트 충전 성공!</h2>
		</div>
		<div class="mb-4">
			<p class="flex items-center text-lg">
				<span class="mr-2">₩</span>충전 금액:
				<span class="font-semibold ml-2">{paymentdata.totalAmount}</span>
			</p>
			<p class="flex items-center text-lg">
				<svg
					class="w-6 h-6 mr-2"
					fill="none"
					stroke="currentColor"
					viewBox="0 0 24 24"
					xmlns="http://www.w3.org/2000/svg"
				>
					<path
						stroke-linecap="round"
						stroke-linejoin="round"
						stroke-width="2"
						d="M17 9V7a4 4 0 00-8 0v2H7v6h10V9h-2zm-4 8a2 2 0 100-4 2 2 0 000 4z"
					></path>
				</svg>결제 수단: <span class="font-semibold ml-2">카드</span>
			</p>
			<p class="flex items-center text-lg">
				<svg
					class="w-6 h-6 mr-2"
					fill="none"
					stroke="currentColor"
					viewBox="0 0 24 24"
					xmlns="http://www.w3.org/2000/svg"
				>
					<path
						stroke-linecap="round"
						stroke-linejoin="round"
						stroke-width="2"
						d="M8 7V3m8 4V3m-9 8h.01M5 12h.01M19 12h.01M12 12v1m0 4v1m-6-6h.01M12 18h0m6-6h.01"
					></path>
				</svg>충전 일시: <span class="font-semibold ml-2">{paymentdata.approvedAt}</span>
			</p>
		</div>
		<div class="flex justify-between">
			<a href="/" class="btn btn-outline">메인화면으로 이동</a>
			<a href="/payment/pay/history" class="btn btn-primary">충전 내역 보기</a>
		</div>
	</div>
</div>
