<script lang="ts">
	import rq from '$lib/rq/rq.svelte';
	import axios from 'axios';

	let input;
	let image;
	let showImage = $state(false);
	let imageSrc = '';
	let content = '';
	let tags: string[] = $state([]);
	let newTag: string = $state('');

	let price = 0;
	let isLoading = $state(false);

	function validateValue() {
		if (price < 0) {
			price = 0;
		} else if (price > 1000) {
			price = 1000;
		} else if (price % 100 !== 0) {
			price = Math.round(price / 100) * 100;
		}
	}

	function addTag() {
		const trimmedTag = newTag.trim();
		if (trimmedTag === '') {
			rq.msgWarning('태그를 입력하세요');
			return;
		} // 빈 태그인 경우 추가하지 않음

		if (tags.includes(trimmedTag)) {
			rq.msgWarning('이미 등록된 태그입니다');
			return;
		}

		if (tags.length >= 5) {
			rq.msgWarning('태그는 최대 5개까지 등록할 수 있습니다');
			return;
		} // 최대 태그 개수를 초과한 경우 추가하지 않음

		tags = [...tags, trimmedTag];
		newTag = '';
	}

	function removeTag(tag: string) {
		tags = tags.filter((t) => t !== tag);
	}
	async function onChange() {
		console.log(showImage);
		if (input.files && input.files[0]) {
			let reader = new FileReader();
			reader.onload = (e) => {
				imageSrc = e.target.result;
				showImage = true;
			};
			reader.readAsDataURL(input.files[0]);
		} else {
			showImage = false;
		}
	}

	async function onSubmit(event) {
		isLoading = true;
		event.preventDefault();
		const file = input.files[0];
		const formData = new FormData();
		formData.append('file', file);
		formData.append('content', content);
		formData.append('tags', tags.join('#'));
		formData.append('price', price);

		axios
			.post('http://localhost:8090/api/v1/image/save', formData, {
				headers: {
					'Content-Type': 'multipart/form-data'
				},
				withCredentials: true
			})
			.then((response) => {
				console.log(response);
				isLoading = false;
				rq.goTo('/');
			})
			.catch((error) => {
				console.error(error);
			});
	}
</script>

<form
	on:submit={onSubmit}
	class="space-y-4 max-w-md mx-auto mt-20 border-2 border-gray-300 p-5 rounded-md"
>
	<div>
		{#if showImage}
			<img bind:this={image} src={imageSrc} alt="Preview" class="w-full h-64 object-contain" />
		{:else}
			<div class="w-full h-64 bg-gray-200 flex items-center justify-center">
				<span>Image Preview</span>
			</div>
		{/if}
	</div>

	<input
		bind:value={content}
		placeholder="Enter title"
		class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
	/>
	<input
		bind:this={input}
		on:change={onChange}
		type="file"
		class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
	/>
	<div class="my-4 flex items-center">
		<input
			class="w-full px-4 py-2 border border-gray-300 rounded-lg mr-2 focus:outline-none focus:ring-2 focus:ring-blue-400"
			id="customInput"
			type="number"
			min="0"
			max="1000"
			step="100"
			bind:value={price}
			on:blur={validateValue}
		/>
		<span>원</span>
	</div>
	<div class="my-4 flex">
		<input
			type="text"
			bind:value={newTag}
			placeholder="태그를 입력하세요"
			class="w-full px-4 py-2 border border-gray-300 rounded-lg mr-2 focus:outline-none focus:ring-2 focus:ring-blue-400"
			on:keypress={(e) => {
				if (e.key === 'Enter') {
					e.preventDefault(); // 기본 동작인 폼 전송을 막습니다.
					addTag();
				}
			}}
		/>
		<button
			on:click={addTag}
			type="button"
			class="w-1/4 px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400"
		>
			추가
		</button>
	</div>
	<div class="my-4">
		{#each tags as tag}
			<span
				class="inline-flex items-center bg-blue-200 text-blue-800 px-2 py-1 rounded-full mr-2 mb-2"
			>
				{tag}
				<button
					on:click={() => removeTag(tag)}
					class="ml-2 bg-red-500 text-white rounded-full h-6 w-6 flex items-center justify-center hover:bg-red-600"
				>
					&times;
				</button>
			</span>
		{/each}
	</div>
	{#if isLoading}
		<button
			type="submit"
			class="w-full px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400"
		>
			<span class="loading loading-spinner loading-xs m-2"></span>
		</button>
	{:else}
		<button
			type="submit"
			class="w-full px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400"
		>
			Upload
		</button>
	{/if}
</form>
