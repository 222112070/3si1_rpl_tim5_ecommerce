document.addEventListener('DOMContentLoaded', function() {
    // Tampilkan overlay saat halaman dimuat
    showLoadingOverlay();

    // Simulasikan waktu pemuatan konten (gantilah dengan konten aktual Anda)
    setTimeout(function() {
        // Sembunyikan overlay setelah konten selesai dimuat
        hideLoadingOverlay();
    }, 2000); // Ganti 2000 dengan waktu pemuatan konten aktual Anda dalam milidetik
});

// Fungsi untuk menampilkan overlay
function showLoadingOverlay() {
    document.getElementById('loadingOverlay').style.display = 'flex';
}

// Fungsi untuk menyembunyikan overlay
function hideLoadingOverlay() {
    document.getElementById('loadingOverlay').style.display = 'none';
}

// Simulate loading progress
let progress = 0;
const loadingPercentageText = document.getElementById("loadingPercentage");

function updateProgress() {
    progress += 1;
    if (progress > 100) {
    } else {
        // Update the loading percentage text
        loadingPercentageText.textContent = `Loading: ${progress}%`;

        // Simulate async loading
        setTimeout(updateProgress, 100);
    }
}

// Start the loading process
updateProgress();