<script lang="ts">
	import rq from '$lib/rq/rq.svelte';
	import axios from 'axios';

	let input;
	let container;
	let image;
	let placeholder;
	let showImage = false;
	let content = '';
	let tags: string[] = $state([]);
	let newTag: string = $state('');

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
		const file = input.files[0];

		if (file) {
			showImage = true;

			const reader = new FileReader();
			reader.addEventListener('load', function () {
				image.setAttribute('src', reader.result);
			});
			reader.readAsDataURL(file);
			return;
		}
		showImage = false;
	}

	async function onSubmit(event) {
		event.preventDefault();
		const file = input.files[0];
		const formData = new FormData();
		formData.append('file', file);
		formData.append('content', content);
		formData.append('tags', tags.join('#'));

		axios
			.post('http://localhost:8090/api/v1/image/save', formData, {
				headers: {
					'Content-Type': 'multipart/form-data'
				},
				withCredentials: true
			})
			.then((response) => {
				console.log(response);
			})
			.catch((error) => {
				console.error(error);
			});
	}
</script>

<h1>Image Preview on File Upload</h1>
<form on:submit={onSubmit}>
	<input bind:value={content} placeholder="Enter title" />
	<input bind:this={input} on:change={onChange} type="file" />
	<button type="submit">Upload</button>
	<div>
		{#if showImage}
			<img bind:this={image} src="" alt="Preview" />
		{:else}
			<span>Image Preview</span>
		{/if}
	</div>

	<div class="my-4">
		<input
			type="text"
			bind:value={newTag}
			placeholder="태그를 입력하세요"
			class="px-4 py-2 border rounded-lg mr-2 focus:outline-none focus:border-gray-700"
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
			class="inline-block px-4 py-2 border border-gray-300 text-gray-700 bg-white hover:bg-black hover:text-white rounded-md shadow-sm text-sm font-medium focus:outline-none"
			>추가</button
		>
	</div>
	<div class="my-4">
		{#each tags as tag}
			<span
				class="inline-flex items-center bg-gray-200 text-gray-800 px-2 py-1 rounded-full mr-2 mb-2"
			>
				<span>{tag}</span>
				<button on:click={() => removeTag(tag)} class="ml-2">&times;</button>
			</span>
		{/each}
	</div>
</form>
