ip a show wlan0 | grep -oP 'inet \K[\d.]+/\d+ brd [\d.]+' >> ed_ip.list && cat ed_ip.list 

