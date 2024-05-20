<script>
	import rq from '$lib/rq/rq.svelte';

	let amount = 0;
	let description = '포인트 충전';
	let error = '';

	async function handleSubmit() {
		if (amount <= 0) {
			error = 'Amount must be greater than zero.';
			rq.msgError(error);
			return;
		} else if (amount > 1000000) {
			error = 'Amount must be less than 1,000,000.';
			rq.msgError(error);
			return;
		} else if (amount % 1000 != 0) {
			error = '천원 단위만 입력할 수 있습니다.';
			rq.msgError(error);
			return;
		}
		alert(`Payment Info:\nAmount: ${amount}\nDescription: ${description}`);
		const data = await rq.apiEndPoints().POST('/api/v1/payment/toss', {
			body: {
				amount,
				orderName: description,
				customerName: rq.member.name,
				payType: 'CARD'
			}
		});
		console.log(data.data?.data.orderId);
		rq.goTo(
			`/payment/pay?id=${description}&orderId=${data.data.data.orderId}&customerName=${rq.member.name}&amount=` +
				amount
		);
	}
</script>

<div class="flex items-center justify-center min-h-screen bg-gray-100 h-screen">
	<div class="bg-white shadow-xl rounded-lg p-8 max-w-md w-full mt-20 h-full">
		<h1 class="text-3xl font-bold mb-6 text-center text-blue-600">Set Payment Information</h1>

		<form on:submit|preventDefault={handleSubmit} class="space-y-6">
			<div class="form-control">
				<label class="label">
					<span class="label-text font-semibold text-gray-700">Amount (₩)</span>
				</label>
				<input type="number" bind:value={amount} min="0" class="input input-bordered w-full" />
			</div>

			<div class="form-control">
				<label class="label">
					<span class="label-text font-semibold text-gray-700">Description</span>
				</label>
				<input type="text" value={description} class="input input-bordered w-full" readonly />
			</div>

			{#if error}
				<div class="text-red-500">{error}</div>
			{/if}

			<button type="submit" class="btn btn-primary w-full mt-4">Submit</button>
		</form>
	</div>
</div>
