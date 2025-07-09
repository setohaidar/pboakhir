#!/bin/bash

echo "🔄 SIPERU Report PDF Converter"
echo "================================="

HTML_FILE="SIPERU_Professional_Report.html"
PDF_FILE="SIPERU_DETAILED_REPORT.pdf"

if [ ! -f "$HTML_FILE" ]; then
    echo "❌ HTML file not found: $HTML_FILE"
    echo "Run 'python3 generate_styled_report.py' first"
    exit 1
fi

echo "📄 HTML file found: $HTML_FILE"
echo "🎯 Target PDF: $PDF_FILE"
echo ""

# Method 1: Try wkhtmltopdf if available
if command -v wkhtmltopdf &> /dev/null; then
    echo "🚀 Method 1: Using wkhtmltopdf..."
    wkhtmltopdf --page-size A4 --margin-top 20mm --margin-bottom 20mm --margin-left 20mm --margin-right 20mm "$HTML_FILE" "$PDF_FILE"
    if [ $? -eq 0 ]; then
        echo "✅ PDF created successfully using wkhtmltopdf!"
        exit 0
    fi
fi

# Method 2: Try chromium/chrome headless
for browser in google-chrome-stable chromium-browser google-chrome chromium; do
    if command -v $browser &> /dev/null; then
        echo "🚀 Method 2: Using $browser headless..."
        $browser --headless --disable-gpu --print-to-pdf="$PDF_FILE" --virtual-time-budget=3000 --print-to-pdf-no-header "$HTML_FILE"
        if [ $? -eq 0 ] && [ -f "$PDF_FILE" ]; then
            echo "✅ PDF created successfully using $browser!"
            exit 0
        fi
    fi
done

# Method 3: Try Firefox headless
if command -v firefox &> /dev/null; then
    echo "🚀 Method 3: Using Firefox headless..."
    firefox --headless --print-to-pdf="$PDF_FILE" "$HTML_FILE" 2>/dev/null
    if [ $? -eq 0 ] && [ -f "$PDF_FILE" ]; then
        echo "✅ PDF created successfully using Firefox!"
        exit 0
    fi
fi

# Method 4: Try pandoc with different engines
if command -v pandoc &> /dev/null; then
    echo "🚀 Method 4: Trying pandoc alternatives..."
    
    # Try with context
    pandoc "$HTML_FILE" -o "$PDF_FILE" --pdf-engine=context 2>/dev/null
    if [ $? -eq 0 ] && [ -f "$PDF_FILE" ]; then
        echo "✅ PDF created successfully using pandoc with context!"
        exit 0
    fi
    
    # Try basic pandoc
    pandoc "$HTML_FILE" -o "$PDF_FILE" 2>/dev/null
    if [ $? -eq 0 ] && [ -f "$PDF_FILE" ]; then
        echo "✅ PDF created successfully using pandoc!"
        exit 0
    fi
fi

# If all methods failed
echo ""
echo "❌ Automatic PDF conversion failed."
echo ""
echo "📝 Manual conversion instructions:"
echo "=================================="
echo "1. Buka file HTML di browser web:"
echo "   file://$(pwd)/$HTML_FILE"
echo ""
echo "2. Tekan Ctrl+P (Windows/Linux) atau Cmd+P (Mac)"
echo ""
echo "3. Pilih 'Save as PDF' atau 'Print to PDF'"
echo ""
echo "4. Pengaturan yang direkomendasikan:"
echo "   - Paper size: A4"
echo "   - Margins: Custom (0.5 inch semua sisi)"
echo "   - Scale: 100%"
echo "   - Headers/Footers: OFF"
echo ""
echo "5. Simpan sebagai: $PDF_FILE"
echo ""

# Create a simple HTML launcher
echo "📄 Creating HTML launcher..."
cat > "open_report.html" << EOF
<!DOCTYPE html>
<html>
<head>
    <title>SIPERU Report Launcher</title>
    <style>
        body { 
            font-family: Arial, sans-serif; 
            text-align: center; 
            padding: 50px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        .container {
            background: white;
            color: #333;
            padding: 30px;
            border-radius: 10px;
            max-width: 600px;
            margin: 0 auto;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        .btn {
            display: inline-block;
            background: #667eea;
            color: white;
            padding: 15px 30px;
            text-decoration: none;
            border-radius: 5px;
            margin: 10px;
            font-size: 18px;
        }
        .btn:hover {
            background: #764ba2;
        }
        h1 { color: #667eea; }
        .instructions {
            text-align: left;
            background: #f8f9fa;
            padding: 20px;
            border-radius: 5px;
            margin: 20px 0;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>📋 SIPERU Report</h1>
        <p>Klik tombol di bawah untuk membuka laporan detail:</p>
        
        <a href="$HTML_FILE" target="_blank" class="btn">📖 Buka Laporan HTML</a>
        
        <div class="instructions">
            <h3>📄 Cara Konversi ke PDF:</h3>
            <ol>
                <li>Klik tombol "Buka Laporan HTML" di atas</li>
                <li>Setelah laporan terbuka, tekan <strong>Ctrl+P</strong> (Windows/Linux) atau <strong>Cmd+P</strong> (Mac)</li>
                <li>Pilih <strong>"Save as PDF"</strong> atau <strong>"Print to PDF"</strong></li>
                <li>Atur margins ke <strong>0.5 inch</strong> untuk hasil terbaik</li>
                <li>Klik <strong>Save</strong> dan pilih lokasi penyimpanan</li>
            </ol>
        </div>
        
        <p><small>Report generated: $(date)</small></p>
    </div>
</body>
</html>
EOF

echo "✅ HTML launcher created: open_report.html"
echo "🌐 Double-click open_report.html untuk memulai!"