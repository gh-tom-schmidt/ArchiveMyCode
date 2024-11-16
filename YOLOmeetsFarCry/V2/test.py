from win32gui import FindWindow, GetWindowRect
window_name = 'FarCry\u00AE5'
window_handle = FindWindow(None, window_name)
window_rect = GetWindowRect(window_handle)
x, y, w, h= window_rect
x += 10
y += 20
w -= 10
h -= 10
window_rect = (x, y, w, h)
print(f"Found window {window_name} at {window_rect}")
