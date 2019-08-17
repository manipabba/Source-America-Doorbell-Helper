#!/usr/bin/env python3

import os
from stat import *                                                                                               
import speech_recognition as sr
#print("coolio")

# get audio from the microphone                                                                       
r = sr.Recognizer()                                                                                   
with sr.Microphone() as source:                                                                       
    print("Speak:")                                                                                   
    audio = r.listen(source)   
 
try:
    #print("You said " + r.recognize_google(audio))
    file = open("speechWords.txt","w+")
    file.write(r.recognize_google(audio))
    file.close()
except sr.UnknownValueError:
    print("Could not understand audio")
except sr.RequestError as e:
    print("Could not request results; {0}".format(e))

