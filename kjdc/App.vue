<script>
	export default {
		onLaunch: function() {
			console.log('快捷点餐系统启动')
			this.initApp()
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		},
		methods: {
			initApp() {
				// 获取用户信息（微信登录）
				uni.getUserProfile({
					desc: '获取您的昵称和头像用于显示',
					success: (res) => {
						uni.setStorage({
							key: 'userInfo',
							data: res.userInfo,
							success: () => {
								console.log('用户信息已保存')
							}
						})
					},
					fail: () => {
						console.log('用户未授权')
					}
				})
			}
		}
	}
</script>

<style>
	/* 全局样式 */
	* {
		margin: 0;
		padding: 0;
		box-sizing: border-box;
	}

	page {
		background-color: #f5f5f5;
		font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
	}

	.btn-primary {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: white;
		border: none;
		border-radius: 8rpx;
		padding: 12rpx 24rpx;
		font-size: 28rpx;
		font-weight: 500;
	}

	.btn-primary:active {
		opacity: 0.8;
	}

	.empty-state {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 100rpx 30rpx;
		color: #999;
	}

	.empty-icon {
		font-size: 80rpx;
		margin-bottom: 20rpx;
	}

	.loading {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 50rpx;
	}
</style>
