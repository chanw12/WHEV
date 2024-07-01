<script>
	import { onMount } from 'svelte';
	import { loadTossPayments } from '@tosspayments/payment-sdk';
	import rq from '$lib/rq/rq.svelte';
	import { v4 as uuidv4 } from 'uuid';

	let clientKey = 'test_ck_oEjb0gm23PbqZpdm6Wk6rpGwBJn5';
	let tossPayments;
	let id;
	let customerName;
	let orderId;
	let amount;

	onMount(async () => {
		const params = new URLSearchParams(window.location.search);
		id = params.get('id');
		customerName = params.get('customerName');
		amount = params.get('amount');
		orderId = params.get('orderId');
		loadTossPayments(clientKey).then((tossPayments) => {
			tossPayments
				.requestPayment('카드', {
					amount,
					orderId,
					orderName: id,
					customerName,
					successUrl: import.meta.env.VITE_CORE_FRONT_BASE_URL + '/payment/pay/success',
					failUrl: import.meta.env.VITE_CORE_FRONT_BASE_URL + '/payment/pay/fail'
				})
				.catch(function (error) {
					console.error(error);
				});
		});
	});
</script>

<svelte:head>
	<title>결제하기</title>
	<script src="https://js.tosspayments.com/v1/payment"></script>
</svelte:head>
