<script lang="ts">
	import rq from '$lib/rq/rq.svelte';
	import axios from 'axios';

	let input;
	let container;
	let image;
	let placeholder;
	let showImage = false;
	let accessToken;
	let refreshToken;

	rq.effect(() => {
		accessToken = localStorage.getItem('accessToken');
		refreshToken = localStorage.getItem('refreshToken');
		axios.defaults.headers.common['accessToken'] = `${accessToken}`;
		axios.defaults.headers.common['refreshToken'] = refreshToken;
	});
	async function onChange() {
		const file = input.files[0];

		if (file) {
			showImage = true;

			const reader = new FileReader();
			reader.addEventListener('load', function () {
				image.setAttribute('src', reader.result);
			});
			reader.readAsDataURL(file);

			// Create a FormData instance
			const formData = new FormData();
			// Append the file to the formData instance
			formData.append('file', file);

			console.log(file);
			for (let [key, value] of formData.entries()) {
				console.log(key, value);
			}
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
			// Send the file to the server
			// rq.apiEndPontsWithImage()
			// 	.POST('/api/v1/image/save', formData)
			// 	.then((response) => {
			// 		console.log(response);
			// 	});

			return;
		}
		showImage = false;
	}
</script>

<h1>Image Preview on File Upload</h1>
<input bind:this={input} on:change={onChange} type="file" />
<div bind:this={container}>
	{#if showImage}
		<img bind:this={image} src="" alt="Preview" />
	{:else}
		<span bind:this={placeholder}>Image Preview</span>
	{/if}
</div>
