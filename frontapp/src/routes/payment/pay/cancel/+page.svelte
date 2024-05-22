<script>
	import rq from '$lib/rq/rq.svelte';
	import { onMount } from 'svelte';

	let returnReason = '';
	let details = '';
	let amount;
	let createDate;
	let customerName;
	let orderName;
	let paymentKey;
	let payType;
	let cancelReason = '';
	onMount(async () => {
		const params = new URLSearchParams(window.location.search);

		const paymentData = await rq.apiEndPoints().GET(`/api/v1/payment/toss/{id}`, {
			params: {
				path: {
					id: params.get('id')
				}
			}
		});
		amount = paymentData.data?.data.amount;
		createDate = paymentData.data?.data.createDate;
		customerName = paymentData.data?.data.customerName;
		paymentKey = paymentData.data?.data.paymentKey;
		payType = paymentData.data?.data.payType;
		orderName = paymentData.data?.data.orderName;
	});
	async function handleSubmit() {
		cancelReason = returnReason + '-' + details;
		const data = await rq.apiEndPoints().POST(`/api/v1/payment/toss/cancel/point`, {
			params: {
				query: {
					paymentKey,
					cancelReason
				}
			}
		});
		console.log(data);
	}
</script>

<main class="p-8 mt-20">
	<div class="mb-6">
		<div class="text-lg font-bold">상품 이름: {orderName}</div>
		<div class="text-lg font-bold">결제 비용: {amount}</div>
		<div class="text-lg font-bold">고객 이름: {customerName}</div>
		<div class="text-lg font-bold">
			결제 날짜: {new Date(createDate).toLocaleString('ko-KR', {
				year: 'numeric',
				month: '2-digit',
				day: '2-digit',
				hour: '2-digit',
				minute: '2-digit'
			})}
		</div>
	</div>

	<div class="form-control w-full max-w-xs mt-4">
		<label class="label">
			<span class="label-text">취소사유를 선택해주세요</span>
		</label>
		<select class="select select-bordered" bind:value={returnReason}>
			<option disabled selected value="">선택하세요</option>
			<option value="잘못결제함">잘못 결제 했습니다.</option>
			<option value="기타">기타</option>
		</select>
	</div>

	<div class="form-control w-full max-w-xs mt-4">
		<label class="label">
			<span class="label-text">상세 사유 입력(선택사항)</span>
		</label>
		<textarea
			class="textarea textarea-bordered"
			bind:value={details}
			placeholder="상세 사유를 입력하세요"
		></textarea>
	</div>

	<button on:click={handleSubmit} class="btn btn-primary mt-4">제출</button>
</main>

<style>
	main {
		max-width: 500px;
		margin: auto;
		margin-top: 60px;
	}
</style>
