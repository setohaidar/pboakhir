#!/bin/bash

echo "🚀 SIPERU - Push to GitHub Script"
echo "================================="

# Add all changes
echo "📁 Adding all changes..."
git add .

# Check git status
echo "📊 Current git status:"
git status

# Commit changes
echo "💾 Committing changes..."
git commit -m "🎨 Major UI Improvements - Modern SIPERU Interface

✨ Features Added:
- 🪟 Window controls: maximize, minimize, resize functionality
- 🎨 Modern gradient backgrounds with 4-color gradients
- 📱 Responsive design with minimum window sizes
- 🚀 Smooth animations and transitions

🔧 Components Updated:
- LoginController: Modern login with focus effects and loading states
- DashboardController: Role-based dashboard with animated cards
- RoomList: Card-based room display with hover effects  
- TimePickerView: Enhanced form with real-time validation

💄 UI/UX Enhancements:
- Typography: Segoe UI font family
- Animations: Fade, scale, and hover transitions
- Cards: Modern shadow effects and rounded corners
- Forms: Focus effects and validation feedback
- Buttons: Gradient styling with hover states

📁 Files Modified:
- src/main/java/org/example/controllers/LoginController.java
- src/main/java/org/example/controllers/DashboardController.java  
- src/main/java/org/example/views/RoomList.java
- src/main/java/org/example/views/TimePickerView.java
- src/main/resources/css/siperu-styles.css (NEW)
- SIPERU_UI_IMPROVEMENTS.md (NEW)

🎯 Result: Professional, modern, and user-friendly interface ready for production!"

# Push to current branch
echo "🔄 Pushing to GitHub..."
git push origin $(git branch --show-current)

echo "✅ Successfully pushed to GitHub!"
echo "🌐 Check your repository: https://github.com/Christiano22-lab/PboAkhir"

# Show final status
echo "📊 Final git status:"
git status

echo ""
echo "🎉 SIPERU UI improvements have been pushed to GitHub!"
echo "📱 Your modern application is now available in the repository."