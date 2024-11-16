import pyautogui
for i, window in enumerate(pyautogui.getAllTitles()):
    print(f"{i}: {window}")