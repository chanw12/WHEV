<script>
	import { onMount } from 'svelte';
	import axios from 'axios';
	import { Gallery } from 'flowbite-svelte';
	let images1 = [];
	let images2 = [];
	let images3 = [];
	let images4 = [];
	let page = 1;
	let isLoading = false;
	let images = [];

	async function loadImages() {
		let tempImages1 = [...images1];
		let tempImages2 = [...images2];
		let tempImages3 = [...images3];
		let tempImages4 = [...images4];
		const response = await axios.get(
			import.meta.env.VITE_CORE_API_BASE_URL + '/api/v1/image?page=' + page
		);
		const newImages = response.data.data.items.content;
		if (newImages.length == 0) {
			isLoading = false;
			return;
		}
		for (let i = 0; i < newImages.length; i++) {
			const imageObject = {
				alt: newImages[i].content,
				id: newImages[i].id,
				member_id: newImages[i].member_id,
				src: import.meta.env.VITE_CORE_API_BASE_URL + '/gen' + newImages[i].path,
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
		console.log(newImages);

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
</script>

{#if isLoading}
	<p>Loading...</p>
{:else}
	<div class="container mx-auto items-center mt-20">
		<Gallery class="gap-4 grid-cols-2 md:grid-cols-4 max-w-100 mx-auto">
			<Gallery items={images1} imgClass="rounded-lg h-auto border-gray-100 border-2" />
			<Gallery items={images2} imgClass="rounded-lg h-auto border-gray-100 border-2" />
			<Gallery items={images3} imgClass="rounded-lg h-auto border-gray-100 border-2 " />
			<Gallery items={images4} imgClass="rounded-lg h-auto border-gray-100 border-2" />
		</Gallery>
	</div>
{/if}
