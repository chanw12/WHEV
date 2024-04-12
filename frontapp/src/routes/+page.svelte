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
	const images5 = [
		{ alt: 'erbology', src: 'https://flowbite.s3.amazonaws.com/docs/gallery/masonry/image.jpg' },
		{ alt: 'shoes', src: 'https://flowbite.s3.amazonaws.com/docs/gallery/masonry/image-1.jpg' },
		{ alt: 'small bag', src: 'https://flowbite.s3.amazonaws.com/docs/gallery/masonry/image-2.jpg' }
	];

	async function loadImages() {
		if (isLoading) return;
		isLoading = true;
		const response = await axios.get(
			import.meta.env.VITE_CORE_API_BASE_URL + '/api/v1/image?page=' + page
		);
		const newImages = response.data.data.items.content;
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
					images1.push(imageObject);
					break;
				case 1:
					images2.push(imageObject);
					break;
				case 2:
					images3.push(imageObject);
					break;
				case 3:
					images4.push(imageObject);
					break;
			}
		}
		console.log(images1[0]);
		console.log(images5[0]);

		images = images.concat(response.data.data.items.content);
		console.log(newImages);
		page++;
		isLoading = false;
	}

	onMount(() => {
		loadImages();

		window.addEventListener('scroll', () => {
			if (window.innerHeight + window.scrollY >= document.documentElement.scrollHeight) {
				if (isLoading == false) {
					loadImages();
					console.log(page);
				}
			}
		});
	});
</script>

{#if isLoading}
	<p>Loading...</p>
{:else}
	<Gallery class="gap-4 grid-cols-2 md:grid-cols-4 max-w-200">
		<Gallery items={images1} imgClass="rounded-lg h-auto w-auto max-w-70" />
		<Gallery items={images2} imgClass="rounded-lg h-auto w-auto max-w-70" />
		<Gallery items={images3} imgClass="rounded-lg h-auto w-auto max-w-70" />
		<Gallery items={images4} imgClass="rounded-lg h-auto w-auto max-w-70" />
	</Gallery>
{/if}

<!-- {#each images as image, i}
	<img
		id={`image-${i}`}
		src="{import.meta.env.VITE_CORE_API_BASE_URL}/gen{image.path}"
		alt={image.title}
	/>
{/each} -->
