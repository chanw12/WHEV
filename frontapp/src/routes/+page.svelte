<script lang="ts">
	import { onMount } from 'svelte';
	import axios from 'axios';
	import Modal from '$lib/components/Modal.svelte';
	import type { components } from '$lib/types/api/v1/schema';
	import rq from '$lib/rq/rq.svelte';

	let images1: components['schemas']['ImageDto'][] = [];
	let images2: components['schemas']['ImageDto'][] = [];
	let images3: components['schemas']['ImageDto'][] = [];
	let images4: components['schemas']['ImageDto'][] = [];
	let page = 1;
	let isLoading = false;
	let images: components['schemas']['ImageDto'][] = [];
	let showModal = false;
	let modalImg: components['schemas']['ImageDto'];
	let comments = [];
	// async function loadComments() {
	// 	const response = await axios.get(import.meta.env.VITE_CORE_API_BASE_URL + '/api/v1/comment');
	// 	comments = response.data.data.items.content;
	// 	console.log(comment)
	// }
	async function loadImages() {
		let tempImages1: components['schemas']['ImageDto'][] = [...images1];
		let tempImages2: components['schemas']['ImageDto'][] = [...images2];
		let tempImages3: components['schemas']['ImageDto'][] = [...images3];
		let tempImages4: components['schemas']['ImageDto'][] = [...images4];
		const response = await axios.get(
			import.meta.env.VITE_CORE_API_BASE_URL + '/api/v1/image?page=' + page
		);
		const newImages = response.data.data.items.content;
		if (newImages.length == 0) {
			isLoading = false;
			return;
		}
		for (let i = 0; i < newImages.length; i++) {
			const imageObject: components['schemas']['ImageDto'] = {
				content: newImages[i].content,
				id: newImages[i].id,
				member_id: newImages[i].member_id,
				path: import.meta.env.VITE_CORE_API_BASE_URL + '/gen' + newImages[i].path,
				tags: newImages[i].tags
			};

			switch (i % 4) {
				case 0:
					tempImages1.push(imageObject);
					break;
				case 1:
					tempImages2.push(imageObject);
					break;
				case 2:
					tempImages3.push(imageObject);
					break;
				case 3:
					tempImages4.push(imageObject);
					break;
			}
		}
		images1 = tempImages1;
		images2 = tempImages2;
		images3 = tempImages3;
		images4 = tempImages4;

		images = [...images, ...newImages];
		page++;
	}

	onMount(() => {
		loadImages();
		window.addEventListener('scroll', () => {
			if (window.innerHeight + window.scrollY >= document.documentElement.scrollHeight) {
				if (isLoading == false) {
					loadImages();
				}
			}
		});
	});
	function showModalFnc(img: components['schemas']['ImageDto']) {
		showModal = true;
		modalImg = img;
		let sse = new EventSource(
			`${import.meta.env.VITE_CORE_API_BASE_URL}/api/sse/subscribe?id=${img.id}`
		);
		sse.addEventListener('addComment', (e) => {});
	}
	async function saveComment() {
		const { data, error } = await rq.apiEndPointsWithFetch(fetch).POST('/api/v1/comment/save', {
			body: {
				content: 'test',
				imageId: modalImg.id
			}
		});
	}
</script>

{#if isLoading}
	<p>Loading...</p>
{:else}
	<Modal bind:showModal>
		<div class="flex w-full h-4/5 mx-auto my-10 overflow-auto gap-20">
			<div class="w-1/2">
				{#if modalImg && modalImg.path}
					<img class="object-cover h-full w-full" src={modalImg.path} alt="" />
				{/if}
			</div>
			<div class="w-1/2 p-4">
				<div class="flex justify-between items-center mb-4">
					<div class="flex gap-2">
						<svg
							xmlns="http://www.w3.org/2000/svg"
							fill="none"
							viewBox="0 0 24 24"
							stroke-width="1.5"
							stroke="currentColor"
							class="w-6 h-6"
						>
							<path
								stroke-linecap="round"
								stroke-linejoin="round"
								d="M9.568 3H5.25A2.25 2.25 0 0 0 3 5.25v4.318c0 .597.237 1.17.659 1.591l9.581 9.581c.699.699 1.78.872 2.607.33a18.095 18.095 0 0 0 5.223-5.223c.542-.827.369-1.908-.33-2.607L11.16 3.66A2.25 2.25 0 0 0 9.568 3Z"
							/>
							<path stroke-linecap="round" stroke-linejoin="round" d="M6 6h.008v.008H6V6Z" />
						</svg>

						<div class="tags text-sm text-gray-500">#tag1 #tag2 #tag3</div>
					</div>

					<div>
						<button class="btn btn-primary mr-2">Like</button>
					</div>
				</div>
				<div class="author text-lg font-bold">Author: John Doe</div>
				<div class="date text-sm text-gray-500">Date: 2022-01-01</div>
				<div class="description mt-2 text-gray-700">This is a brief description of the image.</div>
				<div class="comments mt-4">
					<h3 class="text-lg font-bold">Comments</h3>
					<form class="mt-2" on:submit={saveComment}>
						<input class="input input-bordered w-full" type="text" placeholder="Add a comment..." />
						<button class="btn btn-primary mt-2" type="submit">Post</button>
					</form>
					<div class="posted-comments mt-4">
						<p class="border-b border-gray-200 py-2">Comment 1</p>
						<p class="border-b border-gray-200 py-2">Comment 2</p>
						<!-- Add more comments as needed -->
					</div>
				</div>
			</div>
		</div>
	</Modal>
	<div class="container mx-auto items-center mt-20">
		<div class="grid grid-cols-2 md:grid-cols-4 gap-4">
			<div class="grid gap-4">
				{#each images1 as image}
					<div>
						<img
							on:click={() => showModalFnc(image)}
							class="h-auto max-w-full rounded-lg"
							src={image.path}
						/>
					</div>
				{/each}
			</div>
			<div class="grid gap-4">
				{#each images2 as image}
					<div>
						<img
							on:click={() => showModalFnc(image)}
							class="h-auto max-w-full rounded-lg"
							src={image.path}
						/>
					</div>
				{/each}
			</div>
			<div class="grid gap-4">
				{#each images3 as image}
					<div>
						<img
							on:click={() => showModalFnc(image)}
							class="h-auto max-w-full rounded-lg"
							src={image.path}
						/>
					</div>
				{/each}
			</div>
			<div class="grid gap-4">
				{#each images1 as image}
					<div>
						<img
							on:click={() => showModalFnc(image)}
							class="h-auto max-w-full rounded-lg"
							src={image.path}
						/>
					</div>
				{/each}
			</div>
		</div>
	</div>
{/if}
