from win32gui import FindWindow, GetWindowRect
import bettercam
from PIL import Image
from pynput import keyboard
import os
import time
import threading

# -------------------------------------------------------------------------------------

# because the screen recording runs passive in the background
# we have to get each capured frame on another thread to be able
# to stop the screen capturing
class SaveFrames(threading.Thread):
    def __init__(self, recorder, session_path, count):
        super().__init__()
        self.recorder = recorder
        self.session_path = session_path
        self.count = count
        self.stop_event = threading.Event()

    def getCount(self):
        return self.count

    def run(self):
        while not self.stop_event.is_set() and self.recorder.is_capturing:
            frame = self.recorder.get_latest_frame()  # Waits for a new frame
        
            self.count += 1
            Image.fromarray(frame).save(os.path.join(self.session_path, f'{self.count}.jpg'), 'JPEG', quality=100)
            print(f"Saved: {self.count}.jpg")

    def stop(self):
        self.stop_event.set()

# -----------------------------------------------------------------------------------------------------

class Recorder:
    
    def __init__(self, window_name, output_directory):

        # select the window of intrest
        window_handle = FindWindow(None, window_name)
        window_rect = GetWindowRect(window_handle)
        # crop the window position to cut off the title and border
        x, y, w, h= window_rect
        x += 10
        y += 50
        w -= 10
        h -= 10
        window_rect = (x, y, w, h)
        print(f"Found window {window_name} at {window_rect}")
        
        # define the output directory
        if not os.path.exists(output_directory):
            os.makedirs(output_directory)
            print(f"Directory '{output_directory}' created.")
        else:
            print(f"Directory '{output_directory}' already exists.")

        session_path = os.path.join(directory, time.strftime("%Y-%m-%d-%H-%M-%S"))
        os.mkdir(session_path)
        print(f"Session Path: {session_path}")

        self.window_name = window_name
        self.region = window_rect
        self.session_path = session_path
        self.count = 0
        self.recorder = bettercam.create(output_idx=0, output_color="RGB")

    def on_press(self, key):
        # if F4 and we are recording
        if key == keyboard.Key.f4 and self.recorder.is_capturing:
            self.recorder.stop()
            print("Recording stopped.")

            self.save_frames.stop()
            self.save_frames.join()
            self.count = self.save_frames.getCount()


        # if F3 and we are not recording already -> start recording
        if key == keyboard.Key.f3 and not self.recorder.is_capturing:
            self.save_frames = SaveFrames(self.recorder, self.session_path, self.count)
            
            self.recorder.start(target_fps=1, video_mode=True)
            print("Recording started.")

            self.save_frames.start()


        # if F2 and we are not recording already -> screenshot
        if key == keyboard.Key.f2 and not self.recorder.is_capturing:
            frame = self.recorder.grab(region=self.region)
            
            self.count += 1
            Image.fromarray(frame).save(os.path.join(self.session_path, f'{self.count}.jpg'), 'JPEG', quality=100)
            print(f"Screenshot: {self.count}.jpg")
        
        if key == keyboard.Key.f1:
            # check if we record and stop it
            if self.recorder.is_capturing:
                self.recorder.stop()
                print("Recording stopped.")
            
            return False

    def on_release(self, key):
        pass

    def start(self):
        with keyboard.Listener(on_press=self.on_press, on_release=self.on_release) as listener:
            listener.join()

# -------------------------------------------------------------------------------------

# use windowlist.py to get the name of the desiered window
window_name = 'FarCry\u00AE5'
directory = "Data/Raw/"

r = Recorder(window_name, directory)
r.start()



