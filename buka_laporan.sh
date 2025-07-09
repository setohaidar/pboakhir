#!/bin/bash

echo "🚀 SIPERU Report Launcher"
echo "========================="
echo ""

# Cek file yang tersedia
HTML_SIMPLE="SIPERU_Simple_Report.html"
HTML_PROFESSIONAL="SIPERU_Professional_Report.html"
MARKDOWN="SIPERU_DETAILED_REPORT.md"

echo "📋 File laporan yang tersedia:"
if [ -f "$HTML_SIMPLE" ]; then
    echo "✅ $HTML_SIMPLE (Ringan & Kompatibel)"
fi
if [ -f "$HTML_PROFESSIONAL" ]; then
    echo "✅ $HTML_PROFESSIONAL (Versi Lengkap)"
fi
if [ -f "$MARKDOWN" ]; then
    echo "✅ $MARKDOWN (Format Markdown)"
fi
echo ""

# Coba buka HTML Simple yang baru
if [ -f "$HTML_SIMPLE" ]; then
    echo "🎯 Membuka laporan ringan..."
    
    # Method 1: xdg-open (Linux default)
    if command -v xdg-open > /dev/null 2>&1; then
        echo "📖 Menggunakan xdg-open..."
        xdg-open "$HTML_SIMPLE"
        echo "✅ Laporan telah dibuka di browser!"
        exit 0
    fi
    
    # Method 2: firefox
    if command -v firefox > /dev/null 2>&1; then
        echo "🔥 Menggunakan Firefox..."
        firefox "$HTML_SIMPLE" &
        echo "✅ Laporan telah dibuka di Firefox!"
        exit 0
    fi
    
    # Method 3: google-chrome atau chromium
    if command -v google-chrome > /dev/null 2>&1; then
        echo "🌐 Menggunakan Google Chrome..."
        google-chrome "$HTML_SIMPLE" &
        echo "✅ Laporan telah dibuka di Chrome!"
        exit 0
    fi
    
    if command -v chromium > /dev/null 2>&1; then
        echo "🌐 Menggunakan Chromium..."
        chromium "$HTML_SIMPLE" &
        echo "✅ Laporan telah dibuka di Chromium!"
        exit 0
    fi
    
    # Jika tidak ada browser, berikan instruksi manual
    echo "❌ Browser tidak ditemukan!"
    echo ""
    echo "📋 Cara manual:"
    echo "1. Buka file manager"
    echo "2. Double-click file: $HTML_SIMPLE"
    echo "3. Atau copy path ini ke browser:"
    echo "   file://$(pwd)/$HTML_SIMPLE"
    
else
    echo "❌ File laporan tidak ditemukan!"
fi