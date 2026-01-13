
class CrowdOracle {
    constructor() {
        this.video = document.getElementById('webcam');
        this.canvas = document.getElementById('canvas');
        this.ctx = this.canvas.getContext('2d');
        this.videoOverlay = document.getElementById('videoOverlay');
        this.activeUi = document.getElementById('activeUi');
        this.loadingOverlay = document.getElementById('loadingOverlay');
        this.loadingText = document.getElementById('loadingText');
        
        this.startCameraBtn = document.getElementById('startCameraBtn');
        this.toggleCameraBtn = document.getElementById('toggleCameraBtn');
        this.toggleDetectionBtn = document.getElementById('toggleDetectionBtn');
        this.screenshotBtn = document.getElementById('screenshotBtn');
        this.settingsBtn = document.getElementById('settingsBtn');
        this.closeSettingsBtn = document.getElementById('closeSettingsBtn');
        this.saveSettingsBtn = document.getElementById('saveSettingsBtn');
        this.clearActivityBtn = document.getElementById('clearActivityBtn');
        this.tempUpBtn = document.getElementById('tempUp');
        this.tempDownBtn = document.getElementById('tempDown');
        this.currentCountEl = document.getElementById('currentCount');
        this.maxCapacityDisplay = document.getElementById('maxCapacityDisplay');
        this.occupancyPercentEl = document.getElementById('occupancyPercent');
        this.capacityProgressBar = document.getElementById('capacityProgressBar');
        this.capacityStatusBadge = document.getElementById('capacityStatusBadge');
        this.totalRecordsEl = document.getElementById('totalRecords');
        this.detectionFpsEl = document.getElementById('detectionFps');
        this.temperatureValueEl = document.getElementById('temperatureValue');
        this.connectionStatusText = document.getElementById('connectionStatusText');
        this.activityList = document.getElementById('activityList');
        
        this.roomCapacityInput = document.getElementById('roomCapacity');
        this.modalRoomCapacity = document.getElementById('modalRoomCapacity');
        this.manualTempInput = document.getElementById('manualTemp');
        this.backendUrlInput = document.getElementById('backendUrl');
        this.updateIntervalInput = document.getElementById('updateIntervalInput');
        this.confidenceThresholdInput = document.getElementById('confidenceThreshold');
        this.confidenceValueEl = document.getElementById('confidenceValue');
        this.autoSendDataCheckbox = document.getElementById('autoSendData');
        this.showBoundingBoxesCheckbox = document.getElementById('showBoundingBoxes');
        
        // Modal
        this.settingsModal = document.getElementById('settingsModal');
        
        // State
        this.model = null;
        this.stream = null;
        this.isDetecting = false;
        this.isCameraActive = false;
        this.currentPeopleCount = 0;
        this.totalRecordsSent = 0;
        this.lastDetectionTime = 0;
        this.frameCount = 0;
        this.fps = 0;
        
        // Settings
        this.settings = {
            backendUrl: 'http://localhost:8080',
            updateInterval: 5000, // 5 seconds
            confidenceThreshold: 0.5,
            autoSendData: true,
            showBoundingBoxes: true,
            roomCapacity: 60
        };
        
        // Timers
        this.detectionLoop = null;
        this.dataUploadInterval = null;
        this.fpsInterval = null;
        
        this.init();
    }
    
    async init() {
        this.loadSettings();
        this.bindEvents();
        await this.loadModel();
        this.startFpsCounter();
    }
    
    loadSettings() {
        const savedSettings = localStorage.getItem('crowdOracleSettings');
        if (savedSettings) {
            this.settings = { ...this.settings, ...JSON.parse(savedSettings) };
        }
        this.applySettings();
    }
    
    saveSettings() {
        localStorage.setItem('crowdOracleSettings', JSON.stringify(this.settings));
    }
    
    applySettings() {
        this.backendUrlInput.value = this.settings.backendUrl;
        this.updateIntervalInput.value = this.settings.updateInterval / 1000;
        this.confidenceThresholdInput.value = this.settings.confidenceThreshold;
        this.confidenceValueEl.textContent = this.settings.confidenceThreshold;
        this.autoSendDataCheckbox.checked = this.settings.autoSendData;
        this.showBoundingBoxesCheckbox.checked = this.settings.showBoundingBoxes;
        this.roomCapacityInput.value = this.settings.roomCapacity;
        this.modalRoomCapacity.value = this.settings.roomCapacity;
        this.maxCapacityDisplay.textContent = this.settings.roomCapacity;
        
        document.getElementById('updateInterval').textContent = `${this.settings.updateInterval / 1000}s`;
    }
    
    bindEvents() {
        // Camera buttons
        this.startCameraBtn.addEventListener('click', () => this.startCamera());
        this.toggleCameraBtn.addEventListener('click', () => this.toggleCamera());
        this.toggleDetectionBtn.addEventListener('click', () => this.toggleDetection());
        this.screenshotBtn.addEventListener('click', () => this.takeScreenshot());
        
        // Settings
        this.settingsBtn.addEventListener('click', () => this.openSettings());
        this.closeSettingsBtn.addEventListener('click', () => this.closeSettings());
        this.saveSettingsBtn.addEventListener('click', () => this.saveSettingsFromModal());
        
        // Activity
        this.clearActivityBtn.addEventListener('click', () => this.clearActivity());
        
        this.tempUpBtn.addEventListener('click', () => {
            let val = parseFloat(this.manualTempInput.value);
            val = Math.round((val + 0.1) * 10) / 10;
            this.manualTempInput.value = val;
            this.temperatureValueEl.textContent = val.toFixed(1);
        });
        
        this.tempDownBtn.addEventListener('click', () => {
            let val = parseFloat(this.manualTempInput.value);
            val = Math.round((val - 0.1) * 10) / 10;
            this.manualTempInput.value = val;
            this.temperatureValueEl.textContent = val.toFixed(1);
        });

        this.manualTempInput.addEventListener('change', (e) => {
            this.temperatureValueEl.textContent = parseFloat(e.target.value).toFixed(1);
        });
        
        // Input changes
        this.confidenceThresholdInput.addEventListener('input', (e) => {
            this.confidenceValueEl.textContent = e.target.value;
        });
        
        // Modal backdrop click
        this.settingsModal.addEventListener('click', (e) => {
            if (e.target === this.settingsModal) {
                this.closeSettings();
            }
        });
        
        // Handle window resize
        window.addEventListener('resize', () => this.resizeCanvas());
    }
    
    async loadModel() {
        try {
            this.loadingText.textContent = 'Initializing Neural Engine...';
            this.model = await cocoSsd.load();
            this.loadingText.textContent = 'System Ready';
            
            setTimeout(() => {
                this.loadingOverlay.classList.add('opacity-0', 'pointer-events-none');
                this.showToast('Neural Engine Initialized', 'success');
            }, 800);
            
            this.updateConnectionStatus('Operational');
        } catch (error) {
            console.error('Error loading model:', error);
            this.loadingText.textContent = 'Initialization Failed';
            this.showToast('Failed to load AI model', 'error');
            this.updateConnectionStatus('System Error');
        }
    }
    
    async startCamera() {
        try {
            this.stream = await navigator.mediaDevices.getUserMedia({
                video: {
                    width: { ideal: 1280 },
                    height: { ideal: 720 },
                    facingMode: 'environment'
                },
                audio: false
            });
            
            this.video.srcObject = this.stream;
            
            await new Promise((resolve) => {
                this.video.onloadedmetadata = () => {
                    this.video.play();
                    resolve();
                };
            });
            
            this.resizeCanvas();
            this.videoOverlay.classList.add('opacity-0', 'pointer-events-none');
            this.video.classList.remove('opacity-0');
            this.activeUi.classList.remove('opacity-0');
            this.isCameraActive = true;
            
            this.showToast('Video Feed Established', 'success');
            this.startDetection();
            
        } catch (error) {
            console.error('Error accessing camera:', error);
            this.showToast('Camera Access Denied', 'error');
        }
    }
    
    stopCamera() {
        if (this.stream) {
            this.stream.getTracks().forEach(track => track.stop());
            this.stream = null;
        }
        this.video.srcObject = null;
        this.isCameraActive = false;
        
        this.videoOverlay.classList.remove('opacity-0', 'pointer-events-none');
        this.video.classList.add('opacity-0');
        this.activeUi.classList.add('opacity-0');
        
        this.stopDetection();
    }
    
    toggleCamera() {
        if (this.isCameraActive) {
            this.stopCamera();
            this.showToast('Feed Terminated', 'info');
        } else {
            this.startCamera();
        }
    }
    
    resizeCanvas() {
        if (this.video.videoWidth && this.video.videoHeight) {
            this.canvas.width = this.video.videoWidth;
            this.canvas.height = this.video.videoHeight;
        }
    }
    
    startDetection() {
        if (!this.model || !this.isCameraActive) return;
        
        this.isDetecting = true;
        this.toggleDetectionBtn.classList.add('bg-white/20');
        
        this.detect();
        this.startDataUpload();
    }
    
    stopDetection() {
        this.isDetecting = false;
        this.toggleDetectionBtn.classList.remove('bg-white/20');
        
        if (this.detectionLoop) {
            cancelAnimationFrame(this.detectionLoop);
            this.detectionLoop = null;
        }
        
        this.stopDataUpload();
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
    }
    
    toggleDetection() {
        if (this.isDetecting) {
            this.stopDetection();
            this.showToast('Analysis Paused', 'info');
        } else if (this.isCameraActive) {
            this.startDetection();
            this.showToast('Analysis Resumed', 'success');
        } else {
            this.showToast('Initialize Camera First', 'warning');
        }
    }
    
    async detect() {
        if (!this.isDetecting || !this.model) return;
        
        try {
            const predictions = await this.model.detect(this.video);
            
            // Filter for people only
            const people = predictions.filter(
                p => p.class === 'person' && p.score >= this.settings.confidenceThreshold
            );
            
            this.currentPeopleCount = people.length;
            this.frameCount++;
            
            // Draw detections
            this.drawDetections(people);
            
            // Update UI
            this.updateCapacityDisplay();
            
        } catch (error) {
            console.error('Detection error:', error);
        }
        
        // Continue detection loop
        this.detectionLoop = requestAnimationFrame(() => this.detect());
    }
    
    drawDetections(people) {
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
        
        if (!this.settings.showBoundingBoxes) return;
        
        people.forEach((person, index) => {
            const [x, y, width, height] = person.bbox;
            
            // Draw bounding box - Enterprise Style (Thin, precise lines)
            this.ctx.strokeStyle = '#3B82F6'; // Signal Blue
            this.ctx.lineWidth = 1;
            this.ctx.strokeRect(x, y, width, height);
            
            // Draw corners
            const cornerSize = 10;
            this.ctx.lineWidth = 2;
            
            this.ctx.beginPath();
            // Top-left
            this.ctx.moveTo(x, y + cornerSize);
            this.ctx.lineTo(x, y);
            this.ctx.lineTo(x + cornerSize, y);
            
            // Top-right
            this.ctx.moveTo(x + width - cornerSize, y);
            this.ctx.lineTo(x + width, y);
            this.ctx.lineTo(x + width, y + cornerSize);
            
            // Bottom-right
            this.ctx.moveTo(x + width, y + height - cornerSize);
            this.ctx.lineTo(x + width, y + height);
            this.ctx.lineTo(x + width - cornerSize, y + height);
            
            // Bottom-left
            this.ctx.moveTo(x + cornerSize, y + height);
            this.ctx.lineTo(x, y + height);
            this.ctx.lineTo(x, y + height - cornerSize);
            
            this.ctx.stroke();
            
            // Draw label background
            this.ctx.fillStyle = 'rgba(59, 130, 246, 0.2)';
            this.ctx.fillRect(x, y - 20, width, 20);
            
            // Draw label text
            this.ctx.fillStyle = '#ffffff';
            this.ctx.font = '10px Inter, sans-serif';
            this.ctx.fillText(`TARGET ${index + 1} [${Math.round(person.score * 100)}%]`, x + 5, y - 6);
        });
    }
    
    updateCapacityDisplay() {
        const count = this.currentPeopleCount;
        const capacity = this.settings.roomCapacity;
        const percentage = Math.min(100, Math.round((count / capacity) * 100));
        
        // Update text
        this.currentCountEl.textContent = count;
        this.occupancyPercentEl.textContent = `${percentage}%`;
        
        // Update progress bar width
        this.capacityProgressBar.style.width = `${percentage}%`;
        
        // Update colors based on capacity
        this.capacityProgressBar.className = 'absolute top-0 left-0 h-full transition-all duration-700 ease-out shadow-[0_0_10px_rgba(59,130,246,0.5)]';
        this.capacityStatusBadge.className = 'px-2 py-1 rounded text-[10px] font-medium uppercase tracking-wide border';
        
        if (percentage >= 90) {
            this.capacityProgressBar.classList.add('bg-red-500');
            this.capacityStatusBadge.classList.add('bg-red-500/10', 'border-red-500/20', 'text-red-500');
            this.capacityStatusBadge.textContent = 'Critical';
        } else if (percentage >= 70) {
            this.capacityProgressBar.classList.add('bg-yellow-500');
            this.capacityStatusBadge.classList.add('bg-yellow-500/10', 'border-yellow-500/20', 'text-yellow-500');
            this.capacityStatusBadge.textContent = 'High Load';
        } else {
            this.capacityProgressBar.classList.add('bg-primary');
            this.capacityStatusBadge.classList.add('bg-emerald-500/10', 'border-emerald-500/20', 'text-emerald-500');
            this.capacityStatusBadge.textContent = 'Optimal';
        }
    }
    
    startDataUpload() {
        if (this.dataUploadInterval) {
            clearInterval(this.dataUploadInterval);
        }
        
        // Send data immediately
        this.sendDataToBackend();
        
        // Then send every interval
        this.dataUploadInterval = setInterval(() => {
            if (this.settings.autoSendData) {
                this.sendDataToBackend();
            }
        }, this.settings.updateInterval);
    }
    
    stopDataUpload() {
        if (this.dataUploadInterval) {
            clearInterval(this.dataUploadInterval);
            this.dataUploadInterval = null;
        }
    }
    
    async sendDataToBackend() {
        if (!this.settings.autoSendData) return;
        
        const data = {
            temperatureCelsius: parseFloat(this.manualTempInput.value) || 25.0,
            totalPeopleCount: this.currentPeopleCount
        };
        
        try {
            const response = await fetch(`${this.settings.backendUrl}/api/crowd-data`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });
            
            const result = await response.json();
            
            if (result.success) {
                this.totalRecordsSent++;
                this.totalRecordsEl.textContent = this.totalRecordsSent;
                this.updateConnectionStatus('Operational');
                this.addActivityItem(true, `DATA UPLOAD: ${data.totalPeopleCount} PAX / ${data.temperatureCelsius}°C`);
            } else {
                this.updateConnectionStatus('Sync Error');
                this.addActivityItem(false, 'UPLOAD FAILED');
            }
            
        } catch (error) {
            console.error('Error sending data:', error);
            this.updateConnectionStatus('Offline');
            this.addActivityItem(false, 'CONNECTION LOST');
        }
    }
    
    updateConnectionStatus(text) {
        this.connectionStatusText.textContent = text;
        if (text === 'Operational') {
            this.connectionStatusText.classList.remove('text-red-500');
            this.connectionStatusText.classList.add('text-emerald-500');
        } else {
            this.connectionStatusText.classList.remove('text-emerald-500');
            this.connectionStatusText.classList.add('text-red-500');
        }
    }
    
    addActivityItem(success, message) {
        const emptyState = this.activityList.querySelector('div.text-center');
        if (emptyState) {
            emptyState.remove();
        }
        
        const item = document.createElement('div');
        item.className = `flex items-center gap-3 text-xs py-1 border-l-2 pl-2 ${success ? 'border-emerald-500/50' : 'border-red-500/50'}`;
        item.innerHTML = `
            <span class="text-zinc-600 font-mono">${new Date().toLocaleTimeString([], {hour12: false})}</span>
            <span class="${success ? 'text-zinc-300' : 'text-red-400'}">${message}</span>
        `;
        
        this.activityList.insertBefore(item, this.activityList.firstChild);
        
        // Keep only last 20 items
        while (this.activityList.children.length > 20) {
            this.activityList.removeChild(this.activityList.lastChild);
        }
    }
    
    clearActivity() {
        this.activityList.innerHTML = `
            <div class="text-center py-8">
                <span class="text-xs text-zinc-700">No recent activity</span>
            </div>
        `;
    }
    
    startFpsCounter() {
        this.fpsInterval = setInterval(() => {
            this.fps = this.frameCount;
            this.frameCount = 0;
            this.detectionFpsEl.textContent = this.fps;
        }, 1000);
    }
    
    takeScreenshot() {
        if (!this.isCameraActive) {
            this.showToast('Initialize Camera First', 'warning');
            return;
        }
        
        const screenshotCanvas = document.createElement('canvas');
        screenshotCanvas.width = this.video.videoWidth;
        screenshotCanvas.height = this.video.videoHeight;
        const ctx = screenshotCanvas.getContext('2d');
        
        // Draw video frame
        ctx.drawImage(this.video, 0, 0);
        
        // Draw detection overlay
        ctx.drawImage(this.canvas, 0, 0);
        
        // Download
        const link = document.createElement('a');
        link.download = `crowdoracle-capture-${Date.now()}.png`;
        link.href = screenshotCanvas.toDataURL('image/png');
        link.click();
        
        this.showToast('Evidence Captured', 'success');
    }
    
    openSettings() {
        this.settingsModal.classList.remove('opacity-0', 'pointer-events-none');
        this.settingsModal.querySelector('div').classList.remove('scale-95');
        this.settingsModal.querySelector('div').classList.add('scale-100');
    }
    
    closeSettings() {
        this.settingsModal.classList.add('opacity-0', 'pointer-events-none');
        this.settingsModal.querySelector('div').classList.add('scale-95');
        this.settingsModal.querySelector('div').classList.remove('scale-100');
    }
    
    saveSettingsFromModal() {
        this.settings.backendUrl = this.backendUrlInput.value;
        this.settings.updateInterval = parseInt(this.updateIntervalInput.value) * 1000;
        this.settings.confidenceThreshold = parseFloat(this.confidenceThresholdInput.value);
        this.settings.autoSendData = this.autoSendDataCheckbox.checked;
        this.settings.showBoundingBoxes = this.showBoundingBoxesCheckbox.checked;
        this.settings.roomCapacity = parseInt(this.modalRoomCapacity.value) || 60;
        
        this.saveSettings();
        this.applySettings();
        
        // Restart data upload with new interval if detecting
        if (this.isDetecting) {
            this.startDataUpload();
        }
        
        this.closeSettings();
        this.showToast('Configuration Updated', 'success');
    }
    
    showToast(message, type = 'info') {
        const container = document.getElementById('toastContainer');
        const toast = document.createElement('div');
        toast.className = `toast-enter flex items-center gap-3 px-4 py-3 bg-[#0A0A0A] border border-white/10 rounded shadow-xl min-w-[300px]`;
        
        let iconColor = 'text-blue-500';
        if (type === 'success') iconColor = 'text-emerald-500';
        if (type === 'error') iconColor = 'text-red-500';
        if (type === 'warning') iconColor = 'text-yellow-500';
        
        toast.innerHTML = `
            <div class="w-1.5 h-1.5 rounded-full ${iconColor.replace('text-', 'bg-')}"></div>
            <span class="text-xs font-medium text-white uppercase tracking-wide">${message}</span>
        `;
        
        container.appendChild(toast);
        
        setTimeout(() => {
            toast.style.opacity = '0';
            toast.style.transform = 'translateX(100%)';
            toast.style.transition = 'all 0.3s ease-in';
            setTimeout(() => toast.remove(), 300);
        }, 3000);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    window.crowdOracle = new CrowdOracle();
});
