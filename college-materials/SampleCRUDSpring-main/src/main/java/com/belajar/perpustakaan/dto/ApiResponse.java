package com.belajar.perpustakaan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ==============================================
 * DTO: ApiResponse<T>
 * ==============================================
 * Pembungkus standar untuk semua response REST API.
 * Generic <T> berarti bisa membawa data tipe apapun.
 *
 * Contoh response sukses:
 * {
 *   "success": true,
 *   "message": "Data berhasil diambil",
 *   "data": { ... }
 * }
 *
 * Contoh response error:
 * {
 *   "success": false,
 *   "message": "Buku tidak ditemukan",
 *   "data": null
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

    // Factory method untuk response sukses dengan data
    public static <T> ApiResponse<T> sukses(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    // Factory method untuk response sukses tanpa data
    public static <T> ApiResponse<T> sukses(String message) {
        return new ApiResponse<>(true, message, null);
    }

    // Factory method untuk response error
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
