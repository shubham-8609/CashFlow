# CashFlow (Android)

A simple, clean expense tracker and budget manager for Android. Track spending, set a monthly budget, and get clear feedback as you add, edit, and review expenses.

## Overview
- Add new expenses with amount, title, date, and category.
- Edit or delete existing expenses.
- Set a monthly budget and monitor remaining balance.
- Visual cues warn you as you approach or exceed your budget.
- View detailed information for each expense.

## Screenshots
Below are screenshots to give you a feel for the UI and main flows of the app.

<table>
  <tr>
    <td align="center">
      <img src="screenshots/HomePageImg.png" alt="Home screen" width="240" />
      <div style="font-size:12px"><em>Home screen — budget status & recent expenses</em></div>
    </td>
    <td align="center">
      <img src="screenshots/AddExpenseImg.png" alt="Add expense form" width="240" />
      <div style="font-size:12px"><em>Add expense form — amount, title, category</em></div>
    </td>
    <td align="center">
      <img src="screenshots/ExpenseSavedImg.png" alt="Expense saved confirmation" width="240" />
      <div style="font-size:12px"><em>Expense saved confirmation</em></div>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="screenshots/EditExpenseImg.png" alt="Edit expense form" width="240" />
      <div style="font-size:12px"><em>Edit expense form</em></div>
    </td>
    <td align="center">
      <img src="screenshots/ExpenseDetailsImg.png" alt="Expense details" width="240" />
      <div style="font-size:12px"><em>Expense details</em></div>
    </td>
    <td align="center">
      <img src="screenshots/SetBudgetDialogImg.png" alt="Set budget dialog" width="240" />
      <div style="font-size:12px"><em>Set budget dialog</em></div>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="screenshots/WarningImg.png" alt="Budget warning" width="240" />
      <div style="font-size:12px"><em>Budget warning</em></div>
    </td>
    <td></td>
    <td></td>
  </tr>
</table>

## Features
- Budget tracking: define a spending limit and see your remaining budget.
- Expense management: add, edit, and view detailed entries.
- Helpful feedback: inline confirmations and warnings.
- Simple, intuitive UI built for quick daily use.

## Getting Started
1. Open the project in Android Studio.
2. Let Gradle sync complete.
3. Connect a device or start an emulator.
4. Run the app.

Alternatively, from a terminal you can build the debug APK:

```powershell
./gradlew.bat assembleDebug
```

The APK will be generated under `app/build/outputs/apk/debug/`.

## Folder Structure
- `app/src/main/` – Main application source (Kotlin/Java, resources, manifest)
- `screenshots/` – Images used in this README
- Gradle wrapper files to build from the command line

## Notes
- Screens may vary slightly depending on your device and theme.
- If you don’t see images in this README, ensure you’re viewing it in a context that supports relative image paths (e.g., GitHub, Android Studio’s preview).
