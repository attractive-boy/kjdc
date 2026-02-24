<template>
	<view class="order-container">
		<!-- 订单状态 -->
		<view class="order-status" :class="`status-${order.status.toLowerCase()}`">
			<view class="status-icon">
				<text v-if="order.status === 'PENDING'">⏳</text>
				<text v-else-if="order.status === 'PAID'">✓</text>
				<text v-else-if="order.status === 'COMPLETED'">🎉</text>
				<text v-else>❌</text>
			</view>
			<view class="status-text">
				<text class="status-title">{{ statusText }}</text>
				<text class="status-time">创建于 {{ formatTime(order.createdAt) }}</text>
			</view>
		</view>

		<!-- 订单详情 -->
		<view class="order-details">
			<!-- 订单号 -->
			<view class="detail-section">
				<text class="section-title">订单信息</text>
				<view class="info-row">
					<text class="info-label">订单号</text>
					<text class="info-value">{{ order.orderNo }}</text>
				</view>
				<view class="info-row">
					<text class="info-label">创建时间</text>
					<text class="info-value">{{ formatTime(order.createdAt) }}</text>
				</view>
			</view>

			<!-- 收货地址 -->
			<view class="detail-section">
				<text class="section-title">收货地址</text>
				<view class="address">{{ order.deliveryAddress }}</view>
			</view>

			<!-- 订单内容 -->
			<view class="detail-section">
				<text class="section-title">订单内容</text>
				<view v-for="item in order.items" :key="item.id" class="order-item">
					<view class="item-info">
						<view class="item-name">{{ item.dishName }}</view>
						<view class="item-quantity">×{{ item.quantity }}</view>
					</view>
					<view class="item-price">¥{{ item.subtotal }}</view>
				</view>
			</view>

			<!-- 价格信息 -->
			<view class="detail-section">
				<view class="info-row">
					<text class="info-label">小计</text>
					<text class="info-value">¥{{ calculateSubtotal }}</text>
				</view>
				<view class="info-row">
					<text class="info-label">配送费</text>
					<text class="info-value">¥5.00</text>
				</view>
				<view class="info-row total">
					<text class="info-label">合计</text>
					<text class="info-value">¥{{ order.totalAmount }}</text>
				</view>
			</view>

			<!-- 备注 -->
			<view v-if="order.remarks" class="detail-section">
				<text class="section-title">备注</text>
				<text class="remarks">{{ order.remarks }}</text>
			</view>
		</view>

		<!-- 操作按钮 -->
		<view class="action-buttons">
			<button class="btn-cancel" @click="cancelOrder" v-if="order.status === 'PENDING'">
				取消订单
			</button>
			<button class="btn-back" @click="goBack">返回</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				orderId: null,
				order: {
					id: null,
					orderNo: '',
					status: 'PENDING',
					totalAmount: '0',
					deliveryAddress: '',
					remarks: '',
					items: [],
					createdAt: new Date().toISOString(),
					updatedAt: new Date().toISOString()
				},
				apiUrl: 'http://localhost:8080/api'
			}
		},
		computed: {
			statusText() {
				const statusMap = {
					'PENDING': '待支付',
					'PAID': '已支付',
					'COMPLETED': '已完成',
					'CANCELLED': '已取消'
				}
				return statusMap[this.order.status] || '未知'
			},
			calculateSubtotal() {
				return this.order.items.reduce((sum, item) => {
					return sum + parseFloat(item.subtotal)
				}, 0).toFixed(2)
			}
		},
		onLoad(options) {
			if (options.orderId) {
				this.orderId = options.orderId
				this.loadOrder()
			} else {
				// 从localStorage获取最后一个订单
				uni.getStorage({
					key: 'lastOrder',
					success: (res) => {
						this.order = res.data
					}
				})
			}
		},
		methods: {
			loadOrder() {
				uni.request({
					url: `${this.apiUrl}/orders/${this.orderId}`,
					method: 'GET',
					success: (res) => {
						if (res.data.code === 200) {
							this.order = res.data.data
						}
					},
					fail: () => {
						uni.showToast({ title: '加载订单失败', icon: 'error' })
					}
				})
			},

			cancelOrder() {
				uni.showModal({
					title: '确认取消',
					content: '确定要取消这个订单吗？',
					success: (res) => {
						if (res.confirm) {
							uni.request({
								url: `${this.apiUrl}/orders/${this.orderId}/cancel`,
								method: 'PUT',
								success: (res) => {
									if (res.data.code === 200) {
										this.order = res.data.data
										uni.showToast({ title: '订单已取消', icon: 'success' })
									}
								},
								fail: () => {
									uni.showToast({ title: '取消失败', icon: 'error' })
								}
							})
						}
					}
				})
			},

			formatTime(timeStr) {
				if (!timeStr) return ''
				const date = new Date(timeStr)
				return date.toLocaleString('zh-CN', {
					year: 'numeric',
					month: '2-digit',
					day: '2-digit',
					hour: '2-digit',
					minute: '2-digit'
				})
			},

			goBack() {
				uni.navigateBack()
			}
		}
	}
</script>

<style scoped>
	.order-container {
		display: flex;
		flex-direction: column;
		height: 100vh;
		background-color: #f5f5f5;
	}

	.order-status {
		background: white;
		padding: 20rpx;
		display: flex;
		align-items: center;
		gap: 16rpx;
		border-bottom: 1rpx solid #eee;
	}

	.status-icon {
		font-size: 60rpx;
	}

	.status-text {
		display: flex;
		flex-direction: column;
	}

	.status-title {
		font-size: 28rpx;
		font-weight: bold;
		color: #333;
	}

	.status-time {
		font-size: 24rpx;
		color: #999;
		margin-top: 4rpx;
	}

	.order-status.status-pending {
		background: #fff3cd;
		border-left: 4rpx solid #ffc107;
	}

	.order-status.status-paid {
		background: #d4edda;
		border-left: 4rpx solid #28a745;
	}

	.order-status.status-completed {
		background: #d1ecf1;
		border-left: 4rpx solid #17a2b8;
	}

	.order-status.status-cancelled {
		background: #f8d7da;
		border-left: 4rpx solid #dc3545;
	}

	.order-details {
		flex: 1;
		overflow-y: auto;
		padding: 20rpx;
	}

	.detail-section {
		background: white;
		border-radius: 12rpx;
		padding: 20rpx;
		margin-bottom: 16rpx;
	}

	.section-title {
		font-size: 26rpx;
		font-weight: bold;
		color: #333;
		margin-bottom: 16rpx;
		display: block;
	}

	.info-row {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 12rpx;
		font-size: 26rpx;
		color: #666;
	}

	.info-row.total {
		border-top: 1rpx solid #eee;
		padding-top: 12rpx;
		margin-top: 12rpx;
		font-weight: bold;
		color: #333;
		font-size: 28rpx;
	}

	.info-label {
		color: #999;
	}

	.info-value {
		color: #333;
		font-weight: bold;
	}

	.address {
		font-size: 26rpx;
		color: #333;
		line-height: 1.6;
	}

	.order-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 12rpx 0;
		border-bottom: 1rpx solid #eee;
	}

	.order-item:last-child {
		border-bottom: none;
	}

	.item-info {
		flex: 1;
	}

	.item-name {
		font-size: 26rpx;
		color: #333;
		font-weight: bold;
		margin-bottom: 4rpx;
	}

	.item-quantity {
		font-size: 24rpx;
		color: #999;
	}

	.item-price {
		font-size: 26rpx;
		color: #e74c3c;
		font-weight: bold;
		min-width: 80rpx;
		text-align: right;
	}

	.remarks {
		font-size: 26rpx;
		color: #666;
		line-height: 1.6;
	}

	.action-buttons {
		background: white;
		padding: 20rpx;
		display: flex;
		gap: 12rpx;
		border-top: 1rpx solid #eee;
	}

	.btn-cancel {
		flex: 1;
		background: #e74c3c;
		color: white;
		border: none;
		border-radius: 8rpx;
		padding: 12rpx;
		font-size: 26rpx;
		font-weight: bold;
	}

	.btn-back {
		flex: 1;
		background: #667eea;
		color: white;
		border: none;
		border-radius: 8rpx;
		padding: 12rpx;
		font-size: 26rpx;
		font-weight: bold;
	}
</style>