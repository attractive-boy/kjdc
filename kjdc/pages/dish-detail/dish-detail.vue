<template>
	<view class="detail-container">
		<!-- 菜品图片 -->
		<image :src="dish.imageUrl" class="dish-image" mode="aspectFill"></image>

		<!-- 菜品信息 -->
		<view class="dish-info">
			<view class="dish-header">
				<view>
					<view class="dish-name">{{ dish.name }}</view>
					<view class="dish-rating">⭐ {{ dish.rating || '4.5' }} · 销量 {{ dish.salesVolume || 0 }}</view>
				</view>
			</view>

			<view class="price-section">
				<text class="price">¥{{ dish.price }}</text>
			</view>

			<view class="description">
				<text class="desc-title">商品介绍</text>
				<text class="desc-content">{{ dish.description }}</text>
			</view>

			<view class="category">
				<text class="category-label">分类</text>
				<text class="category-value">{{ dish.category }}</text>
			</view>
		</view>

		<!-- 操作按钮 -->
		<view class="action-buttons">
			<button class="btn-minus" @click="decreaseQuantity">−</button>
			<text class="quantity">{{ quantity }}</text>
			<button class="btn-plus" @click="increaseQuantity">+</button>
			<button class="btn-add-cart" @click="addToCart">加入购物车</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				dish: {
					name: '',
					description: '',
					price: '0',
					category: '',
					imageUrl: '',
					rating: '4.5',
					salesVolume: 0
				},
				quantity: 1,
				dishId: null,
				apiUrl: 'http://localhost:8080/api'
			}
		},
		onLoad(options) {
			this.dishId = options.id
			this.loadDishDetail()
		},
		methods: {
			loadDishDetail() {
				uni.request({
					url: `${this.apiUrl}/dishes/${this.dishId}`,
					method: 'GET',
					success: (res) => {
						if (res.data.code === 200) {
							this.dish = res.data.data
						}
					},
					fail: () => {
						uni.showToast({ title: '加载菜品失败', icon: 'error' })
					}
				})
			},

			increaseQuantity() {
				this.quantity++
			},

			decreaseQuantity() {
				if (this.quantity > 1) {
					this.quantity--
				}
			},

			addToCart() {
				uni.getStorage({
					key: 'cart',
					success: (res) => {
						let cart = res.data || []
						let item = cart.find(d => d.id === this.dishId)
						if (item) {
							item.quantity += this.quantity
						} else {
							cart.push({ ...this.dish, id: this.dishId, quantity: this.quantity })
						}
						uni.setStorage({
							key: 'cart',
							data: cart,
							success: () => {
								uni.showToast({ title: '已加入购物车', icon: 'success' })
								setTimeout(() => {
									uni.navigateBack()
								}, 1000)
							}
						})
					},
					fail: () => {
						let cart = [{ ...this.dish, id: this.dishId, quantity: this.quantity }]
						uni.setStorage({
							key: 'cart',
							data: cart,
							success: () => {
								uni.showToast({ title: '已加入购物车', icon: 'success' })
								setTimeout(() => {
									uni.navigateBack()
								}, 1000)
							}
						})
					}
				})
			}
		}
	}
</script>

<style scoped>
	.detail-container {
		display: flex;
		flex-direction: column;
		height: 100vh;
		background-color: #f5f5f5;
	}

	.dish-image {
		width: 100%;
		height: 300rpx;
		object-fit: cover;
	}

	.dish-info {
		flex: 1;
		background: white;
		padding: 20rpx;
		border-bottom: 1rpx solid #eee;
		overflow-y: auto;
	}

	.dish-header {
		margin-bottom: 20rpx;
	}

	.dish-name {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
		margin-bottom: 8rpx;
	}

	.dish-rating {
		font-size: 24rpx;
		color: #999;
	}

	.price-section {
		margin-bottom: 20rpx;
		padding-bottom: 20rpx;
		border-bottom: 1rpx solid #eee;
	}

	.price {
		font-size: 40rpx;
		color: #e74c3c;
		font-weight: bold;
	}

	.description {
		margin-bottom: 20rpx;
	}

	.desc-title {
		font-size: 28rpx;
		font-weight: bold;
		color: #333;
		display: block;
		margin-bottom: 10rpx;
	}

	.desc-content {
		font-size: 26rpx;
		color: #666;
		line-height: 1.6;
	}

	.category {
		padding: 12rpx 0;
		border-top: 1rpx solid #eee;
	}

	.category-label {
		font-size: 26rpx;
		color: #999;
		margin-right: 20rpx;
	}

	.category-value {
		font-size: 26rpx;
		color: #333;
		font-weight: bold;
	}

	.action-buttons {
		background: white;
		padding: 20rpx;
		display: flex;
		gap: 12rpx;
		align-items: center;
		border-top: 1rpx solid #eee;
	}

	.btn-minus,
	.btn-plus {
		width: 60rpx;
		height: 60rpx;
		border: 1rpx solid #ddd;
		background: #f0f0f0;
		border-radius: 4rpx;
		font-size: 28rpx;
		padding: 0;
	}

	.quantity {
		min-width: 50rpx;
		text-align: center;
		font-size: 26rpx;
		font-weight: bold;
	}

	.btn-add-cart {
		flex: 1;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: white;
		border: none;
		border-radius: 8rpx;
		padding: 12rpx;
		font-size: 28rpx;
		font-weight: bold;
	}
</style>