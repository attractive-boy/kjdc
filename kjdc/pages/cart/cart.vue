<template>
	<view class="cart-container">
		<!-- 顶部导航 -->
		<view class="cart-header">
			<text class="header-title">购物车</text>
			<text v-if="cartItems.length > 0" class="clear-btn" @click="clearCart">清空</text>
		</view>

		<!-- 购物车项列表 -->
		<view v-if="cartItems.length > 0" class="cart-items">
			<view v-for="(item, idx) in cartItems" :key="idx" class="cart-item">
				<image :src="item.imageUrl" class="item-image"></image>
				<view class="item-content">
					<view class="item-name">{{ item.name }}</view>
					<view class="item-price">¥{{ item.price }}</view>
				</view>
				<view class="quantity-control">
					<button class="qty-btn" @click="decreaseQuantity(idx)">−</button>
					<text class="qty">{{ item.quantity }}</text>
					<button class="qty-btn" @click="increaseQuantity(idx)">+</button>
				</view>
				<button class="delete-btn" @click="deleteItem(idx)">🗑</button>
			</view>
		</view>

		<!-- 空购物车 -->
		<view v-else class="empty-state">
			<text class="empty-icon">🛒</text>
			<text>购物车为空</text>
			<button class="btn-back" @click="goBack">继续购物</button>
		</view>

		<!-- 结算信息 -->
		<view v-if="cartItems.length > 0" class="checkout-section">
			<view class="checkout-info">
				<view class="info-row">
					<text>小计</text>
					<text class="info-value">¥{{ subtotal }}</text>
				</view>
				<view class="info-row">
					<text>配送费</text>
					<text class="info-value">¥{{ deliveryFee }}</text>
				</view>
				<view class="info-row total">
					<text>合计</text>
					<text class="info-value">¥{{ total }}</text>
				</view>
			</view>

			<!-- 收货地址 -->
			<view class="address-section">
				<text class="section-title">收货地址</text>
				<view class="address-input">
					<input 
						type="text" 
						placeholder="请输入收货地址"
						v-model="deliveryAddress"
						class="input"
					/>
				</view>
			</view>

			<!-- 备注 -->
			<view class="remarks-section">
				<text class="section-title">备注</text>
				<view class="remarks-input">
					<textarea 
						placeholder="请输入订单备注（可选）"
						v-model="remarks"
						class="textarea"
					></textarea>
				</view>
			</view>

			<!-- 结算按钮 -->
			<button class="btn-checkout" @click="submitOrder">提交订单</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				cartItems: [],
				deliveryAddress: '',
				remarks: '',
				deliveryFee: 5,
				apiUrl: 'http://localhost:8080/api',
				userId: 1 // 默认用户ID，实际应从登录获取
			}
		},
		computed: {
			subtotal() {
				return this.cartItems.reduce((sum, item) => {
					return sum + (parseFloat(item.price) * item.quantity)
				}, 0).toFixed(2)
			},
			total() {
				return (parseFloat(this.subtotal) + this.deliveryFee).toFixed(2)
			}
		},
		onLoad() {
			this.loadCart()
		},
		methods: {
			loadCart() {
				uni.getStorage({
					key: 'cart',
					success: (res) => {
						this.cartItems = res.data || []
					}
				})
			},

			increaseQuantity(idx) {
				this.cartItems[idx].quantity++
				this.saveCart()
			},

			decreaseQuantity(idx) {
				if (this.cartItems[idx].quantity > 1) {
					this.cartItems[idx].quantity--
				} else {
					this.deleteItem(idx)
				}
				this.saveCart()
			},

			deleteItem(idx) {
				this.cartItems.splice(idx, 1)
				this.saveCart()
			},

			clearCart() {
				uni.showModal({
					title: '确认清空',
					content: '确定要清空购物车吗？',
					success: (res) => {
						if (res.confirm) {
							this.cartItems = []
							this.saveCart()
						}
					}
				})
			},

			saveCart() {
				uni.setStorage({
					key: 'cart',
					data: this.cartItems
				})
			},

			submitOrder() {
				if (!this.deliveryAddress.trim()) {
					uni.showToast({ title: '请输入收货地址', icon: 'none' })
					return
				}

				if (this.cartItems.length === 0) {
					uni.showToast({ title: '购物车为空', icon: 'none' })
					return
				}

				// 构建订单请求
				const orderRequest = {
					userId: this.userId,
					deliveryAddress: this.deliveryAddress,
					remarks: this.remarks,
					items: this.cartItems.map(item => ({
						dishId: item.id,
						quantity: item.quantity
					}))
				}

				uni.showLoading({ title: '提交订单中...' })

				uni.request({
					url: `${this.apiUrl}/orders`,
					method: 'POST',
					header: {
						'Content-Type': 'application/json'
					},
					data: orderRequest,
					success: (res) => {
						uni.hideLoading()
						if (res.data.code === 200) {
							uni.showToast({ title: '订单提交成功！', icon: 'success' })
							// 清空购物车
							this.cartItems = []
							this.saveCart()
							// 保存订单信息
							uni.setStorage({
								key: 'lastOrder',
								data: res.data.data
							})
							// 重定向到订单页面
							setTimeout(() => {
								uni.navigateTo({
									url: `/pages/order/order?orderId=${res.data.data.id}`
								})
							}, 1500)
						} else {
							uni.showToast({ title: res.data.message || '订单提交失败', icon: 'error' })
						}
					},
					fail: () => {
						uni.hideLoading()
						uni.showToast({ title: '网络错误', icon: 'error' })
					}
				})
			},

			goBack() {
				uni.navigateBack()
			}
		}
	}
</script>

<style scoped>
	.cart-container {
		display: flex;
		flex-direction: column;
		height: 100vh;
		background-color: #f5f5f5;
	}

	.cart-header {
		background: white;
		padding: 20rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		box-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.05);
	}

	.header-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
	}

	.clear-btn {
		color: #e74c3c;
		font-size: 26rpx;
		cursor: pointer;
	}

	.cart-items {
		flex: 1;
		overflow-y: auto;
		padding: 20rpx;
		display: flex;
		flex-direction: column;
		gap: 12rpx;
	}

	.cart-item {
		background: white;
		border-radius: 12rpx;
		padding: 16rpx;
		display: flex;
		align-items: center;
		gap: 12rpx;
		box-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.05);
	}

	.item-image {
		width: 100rpx;
		height: 100rpx;
		border-radius: 8rpx;
		object-fit: cover;
	}

	.item-content {
		flex: 1;
	}

	.item-name {
		font-size: 28rpx;
		font-weight: bold;
		color: #333;
		margin-bottom: 4rpx;
	}

	.item-price {
		font-size: 26rpx;
		color: #e74c3c;
		font-weight: bold;
	}

	.quantity-control {
		display: flex;
		align-items: center;
		gap: 8rpx;
	}

	.qty-btn {
		width: 40rpx;
		height: 40rpx;
		border: 1rpx solid #ddd;
		background: #f0f0f0;
		border-radius: 4rpx;
		padding: 0;
		font-size: 20rpx;
	}

	.qty {
		min-width: 30rpx;
		text-align: center;
		font-size: 24rpx;
	}

	.delete-btn {
		width: 40rpx;
		height: 40rpx;
		border: none;
		background: transparent;
		font-size: 24rpx;
		padding: 0;
	}

	.empty-state {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		flex: 1;
		color: #999;
	}

	.empty-icon {
		font-size: 100rpx;
		margin-bottom: 20rpx;
	}

	.btn-back {
		background: #667eea;
		color: white;
		border: none;
		border-radius: 8rpx;
		padding: 12rpx 24rpx;
		margin-top: 30rpx;
		font-size: 28rpx;
	}

	.checkout-section {
		background: white;
		padding: 20rpx;
		border-top: 1rpx solid #eee;
	}

	.checkout-info {
		margin-bottom: 20rpx;
		padding-bottom: 20rpx;
		border-bottom: 1rpx solid #eee;
	}

	.info-row {
		display: flex;
		justify-content: space-between;
		margin-bottom: 12rpx;
		font-size: 26rpx;
		color: #666;
	}

	.info-row.total {
		font-weight: bold;
		color: #333;
		font-size: 28rpx;
		margin-top: 12rpx;
	}

	.info-value {
		color: #e74c3c;
		font-weight: bold;
	}

	.address-section,
	.remarks-section {
		margin-bottom: 20rpx;
	}

	.section-title {
		font-size: 26rpx;
		font-weight: bold;
		color: #333;
		margin-bottom: 10rpx;
		display: block;
	}

	.address-input,
	.remarks-input {
		background: #f0f0f0;
		border-radius: 8rpx;
		padding: 12rpx;
	}

	.input,
	.textarea {
		width: 100%;
		background: transparent;
		border: none;
		font-size: 26rpx;
		color: #333;
		outline: none;
	}

	.textarea {
		min-height: 80rpx;
		resize: vertical;
	}

	.btn-checkout {
		width: 100%;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: white;
		border: none;
		border-radius: 8rpx;
		padding: 16rpx;
		font-size: 28rpx;
		font-weight: bold;
		margin-top: 20rpx;
	}
</style>