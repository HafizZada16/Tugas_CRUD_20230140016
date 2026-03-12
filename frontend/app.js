/* ======================================================
   SISTEM CRUD KTP - app.js
   jQuery Ajax untuk operasi CRUD tanpa refresh halaman
   ====================================================== */

const API_URL = 'http://localhost:8080/ktp';

let pendingDeleteId = null;  // Menyimpan id yang akan dihapus

// =====================================================
// DOCUMENT READY
// =====================================================
$(document).ready(function () {
    loadAllKtp();

    // ── Theme Toggle ──────────────────────────────
    // Load saved theme (default: light)
    const savedTheme = localStorage.getItem('theme') || 'light';
    if (savedTheme === 'dark') applyDark();

    $('#btn-theme').on('click', function () {
        if ($('body').hasClass('dark')) {
            applyLight();
        } else {
            applyDark();
        }
    });

    function applyDark() {
        $('body').addClass('dark');
        $('#theme-icon').text('☀️');
        $('#theme-label').text('Light Mode');
        localStorage.setItem('theme', 'dark');
    }

    function applyLight() {
        $('body').removeClass('dark');
        $('#theme-icon').text('🌙');
        $('#theme-label').text('Dark Mode');
        localStorage.setItem('theme', 'light');
    }
    // ─────────────────────────────────────────────

    // Submit form (Create / Update)
    $('#ktp-form').on('submit', function (e) {
        e.preventDefault();
        const id = $('#ktp-id').val();
        if (id) {
            updateKtp(id);
        } else {
            createKtp();
        }
    });

    // Batal edit
    $('#btn-cancel').on('click', function () {
        resetForm();
    });

    // Refresh tabel
    $('#btn-refresh').on('click', function () {
        loadAllKtp();
    });

    // Konfirmasi hapus
    $('#btn-confirm-delete').on('click', function () {
        if (pendingDeleteId !== null) {
            deleteKtp(pendingDeleteId);
        }
    });

    // Batal hapus
    $('#btn-cancel-delete').on('click', function () {
        closeModal();
    });

    // Tutup modal klik di luar
    $('#modal-overlay').on('click', function (e) {
        if ($(e.target).is('#modal-overlay')) {
            closeModal();
        }
    });
});

// =====================================================
// GET ALL - Ambil semua data KTP
// =====================================================
function loadAllKtp() {
    $('#ktp-tbody').html(`
        <tr id="row-loading">
            <td colspan="7" class="table-loading">
                <div class="spinner"></div> Memuat data...
            </td>
        </tr>
    `);
    $('#table-empty').hide();
    $('#record-count').text('Memuat...');

    $.ajax({
        url: API_URL,
        method: 'GET',
        dataType: 'json',
        success: function (res) {
            if (res.success && res.data.length > 0) {
                renderTable(res.data);
                $('#record-count').text(res.data.length + ' data');
                $('#table-empty').hide();
            } else {
                $('#ktp-tbody').empty();
                $('#table-empty').show();
                $('#record-count').text('0 data');
            }
        },
        error: function (xhr) {
            $('#ktp-tbody').empty();
            showToast('Gagal memuat data: ' + getErrorMessage(xhr), 'error');
            $('#record-count').text('Error');
        }
    });
}

// =====================================================
// Render baris tabel dari data array
// =====================================================
function renderTable(data) {
    const tbody = $('#ktp-tbody');
    tbody.empty();

    data.forEach(function (ktp, index) {
        const genderBadge = ktp.jenisKelamin === 'Laki-laki'
            ? `<span class="gender-badge gender-laki">👨 Laki-laki</span>`
            : `<span class="gender-badge gender-perempuan">👩 Perempuan</span>`;

        const row = `
            <tr id="row-${ktp.id}">
                <td>${index + 1}</td>
                <td><code style="color:#a5b4fc;font-size:0.82rem;">${ktp.nomorKtp}</code></td>
                <td><strong>${escapeHtml(ktp.namaLengkap)}</strong></td>
                <td style="max-width:200px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;"
                    title="${escapeHtml(ktp.alamat)}">${escapeHtml(ktp.alamat)}</td>
                <td>${formatDate(ktp.tanggalLahir)}</td>
                <td>${genderBadge}</td>
                <td>
                    <div class="action-cell">
                        <button class="btn btn-edit" onclick="prepareEdit(${ktp.id})">✏️ Edit</button>
                        <button class="btn btn-delete" onclick="confirmDelete(${ktp.id}, '${escapeHtml(ktp.namaLengkap)}')">🗑️ Hapus</button>
                    </div>
                </td>
            </tr>
        `;
        tbody.append(row);
    });
}

// =====================================================
// GET BY ID - Isi form untuk Edit
// =====================================================
function prepareEdit(id) {
    $.ajax({
        url: API_URL + '/' + id,
        method: 'GET',
        dataType: 'json',
        success: function (res) {
            if (res.success) {
                const ktp = res.data;
                $('#ktp-id').val(ktp.id);
                $('#nomorKtp').val(ktp.nomorKtp);
                $('#namaLengkap').val(ktp.namaLengkap);
                $('#alamat').val(ktp.alamat);
                $('#tanggalLahir').val(ktp.tanggalLahir);
                $('#jenisKelamin').val(ktp.jenisKelamin);

                $('#form-title').text('✏️ Edit Data KTP - ID: ' + ktp.id);
                $('#btn-submit').html('<span class="btn-icon">💾</span> Update Data');
                $('#btn-cancel').show();

                // Scroll ke form
                $('html, body').animate({ scrollTop: $('.form-card').offset().top - 80 }, 400);
                clearErrors();
            }
        },
        error: function (xhr) {
            showToast('Gagal memuat data: ' + getErrorMessage(xhr), 'error');
        }
    });
}

// =====================================================
// POST - Tambah data KTP baru
// =====================================================
function createKtp() {
    const payload = buildPayload();
    clearErrors();

    $.ajax({
        url: API_URL,
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(payload),
        dataType: 'json',
        success: function (res) {
            if (res.success) {
                showToast('✅ Data KTP berhasil ditambahkan!', 'success');
                resetForm();
                loadAllKtp();
            }
        },
        error: function (xhr) {
            handleError(xhr);
        }
    });
}

// =====================================================
// PUT - Update data KTP berdasarkan id
// =====================================================
function updateKtp(id) {
    const payload = buildPayload();
    clearErrors();

    $.ajax({
        url: API_URL + '/' + id,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(payload),
        dataType: 'json',
        success: function (res) {
            if (res.success) {
                showToast('✅ Data KTP berhasil diperbarui!', 'success');
                resetForm();
                loadAllKtp();
            }
        },
        error: function (xhr) {
            handleError(xhr);
        }
    });
}

// =====================================================
// DELETE - Tampilkan modal konfirmasi
// =====================================================
function confirmDelete(id, nama) {
    pendingDeleteId = id;
    $('#modal-text').text('Apakah kamu yakin ingin menghapus data atas nama "' + nama + '"?');
    $('#modal-overlay').show();
}

function deleteKtp(id) {
    closeModal();
    $.ajax({
        url: API_URL + '/' + id,
        method: 'DELETE',
        dataType: 'json',
        success: function (res) {
            if (res.success) {
                showToast('🗑️ Data KTP berhasil dihapus!', 'success');
                $('#row-' + id).fadeOut(300, function () {
                    $(this).remove();
                    // Cek apakah tabel kosong
                    if ($('#ktp-tbody tr').length === 0) {
                        $('#table-empty').show();
                        $('#record-count').text('0 data');
                    } else {
                        // Update nomor urut
                        loadAllKtp();
                    }
                });
            }
        },
        error: function (xhr) {
            showToast('Gagal menghapus: ' + getErrorMessage(xhr), 'error');
        }
    });
}

// =====================================================
// HELPERS
// =====================================================

function buildPayload() {
    return {
        nomorKtp:    $('#nomorKtp').val().trim(),
        namaLengkap: $('#namaLengkap').val().trim(),
        alamat:      $('#alamat').val().trim(),
        tanggalLahir: $('#tanggalLahir').val(),
        jenisKelamin: $('#jenisKelamin').val()
    };
}

function resetForm() {
    $('#ktp-form')[0].reset();
    $('#ktp-id').val('');
    $('#form-title').text('➕ Tambah Data KTP');
    $('#btn-submit').html('<span class="btn-icon">💾</span> Simpan Data');
    $('#btn-cancel').hide();
    clearErrors();
    $('.form-input').removeClass('is-error');
}

function clearErrors() {
    $('.form-error').text('');
    $('.form-input').removeClass('is-error');
}

function handleError(xhr) {
    const res = xhr.responseJSON;

    if (xhr.status === 400 && res && res.data) {
        // Validasi field — tampilkan error per field
        $.each(res.data, function (field, message) {
            $('#err-' + field).text(message);
            $('#' + field).addClass('is-error');
        });
        showToast('⚠️ Periksa kembali data yang dimasukkan.', 'error');
    } else {
        const msg = res ? res.message : 'Terjadi kesalahan pada server.';
        showToast('❌ ' + msg, 'error');
    }
}

function getErrorMessage(xhr) {
    return xhr.responseJSON ? xhr.responseJSON.message : 'Terjadi kesalahan.';
}

function formatDate(dateStr) {
    if (!dateStr) return '-';
    const [y, m, d] = dateStr.split('-');
    const bulan = ['Jan','Feb','Mar','Apr','Mei','Jun','Jul','Agu','Sep','Okt','Nov','Des'];
    return `${d} ${bulan[parseInt(m) - 1]} ${y}`;
}

function escapeHtml(str) {
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
}

function closeModal() {
    $('#modal-overlay').hide();
    pendingDeleteId = null;
}

// =====================================================
// TOAST NOTIFICATION
// =====================================================
let toastTimer = null;

function showToast(message, type = 'info') {
    const toast = $('#toast');
    if (toastTimer) clearTimeout(toastTimer);

    toast.removeClass('toast-success toast-error toast-info show');
    toast.text(message).addClass('toast-' + type);

    // Tunggu sebentar agar transisi berjalan ulang
    setTimeout(function () {
        toast.addClass('show');
    }, 10);

    toastTimer = setTimeout(function () {
        toast.removeClass('show');
    }, 3500);
}
