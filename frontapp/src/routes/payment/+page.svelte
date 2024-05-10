<script>
	import { onMount } from 'svelte';
	import { loadTossPayments } from '@tosspayments/payment-sdk';
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
		orderId = uuidv4();
		customerName = params.get('customerName');
		amount = params.get('amount');

		loadTossPayments(clientKey).then((tossPayments) => {
			tossPayments
				.requestPayment('카드', {
					amount,
					orderId,
					orderName: id,
					customerName,
					successUrl: 'https://docs.tosspayments.com/guides/payment/test-success',
					failUrl: 'https://docs.tosspayments.com/guides/payment/test-fail'
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
