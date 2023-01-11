#!/usr/bin/env python
import sys

if len(sys.argv) != 2:
  print("Whoops too many args :(")
  exit()

def dofix(b):
  return str(hex(b))[2:].rjust(2, '0').upper()

source = sys.argv[1]
with open(f"{source}.output", 'w') as output:
  with open(source, 'rb') as f:
    while 1:
      byte_s = f.read(1)
      if not byte_s:
        break
      byte = byte_s[0]
      converted_byte = dofix(byte)
      output.write(converted_byte)
      if converted_byte == "00":
        output.write(" : nop\n")
      elif converted_byte == "01":
        output.write(" : aconst_null\n")
      elif converted_byte == "02":
        output.write(" : iconst_m1\n")
      elif converted_byte == "03":
        output.write(" : iconst_0\n")
      elif converted_byte == "04":
        output.write(" : iconst_1\n")
      elif converted_byte == "05":
        output.write(" : iconst_2\n")
      elif converted_byte == "06":
        output.write(" : iconst_3\n")
      elif converted_byte == "07":
        output.write(" : iconst_4\n")
      elif converted_byte == "08":
        output.write(" : iconst_5\n")
      elif converted_byte == "09":
        output.write(" : lconst_0\n")
      elif converted_byte == "0A":
        output.write(" : lconst_1\n")
      elif converted_byte == "0B":
        output.write(" : fconst_0\n")
      elif converted_byte == "0C":
        output.write(" : fconst_1\n")
      elif converted_byte == "0D":
        output.write(" : fconst_2\n")
      elif converted_byte == "0E":
        output.write(" : dconst_0\n")
      elif converted_byte == "0F":
        output.write(" : dconst_1\n")
      elif converted_byte == "10":
        v = f.read(1)
        if not v:
          print("ERROR")
          break
        nextbyte = dofix(v[0])
        output.write(" ")
        output.write(nextbyte)
        output.write(" : bipush\n")
      elif converted_byte == "11":
        v = f.read(1)
        if not v:
          print("ERROR")
          break
        nextbyte = dofix(v[0])
        output.write(" ")
        output.write(nextbyte)
        v = f.read(1)
        if not v:
          print("ERROR")
          break
        nextbyte = dofix(v[0])
        output.write(" ")
        output.write(nextbyte)
        output.write(" : sipush\n")
      elif converted_byte == "12":
        v = f.read(1)
        if not v:
          print("ERROR")
          break
        nextbyte = dofix(v[0])
        output.write(" ")
        output.write(nextbyte)
        output.write(" : ldc\n")
      elif converted_byte == "13":
        v = f.read(1)
        if not v:
          print("ERROR")
          break
        nextbyte = dofix(v[0])
        output.write(" ")
        output.write(nextbyte)
        v = f.read(1)
        if not v:
          print("ERROR")
          break
        nextbyte = dofix(v[0])
        output.write(" ")
        output.write(nextbyte)
        output.write(" : ldc_w\n")
      elif converted_byte == "14":
        v = f.read(1)
        if not v:
          print("ERROR")
          break
        nextbyte = dofix(v[0])
        output.write(" ")
        output.write(nextbyte)
        v = f.read(1)
        if not v:
          print("ERROR")
          break
        nextbyte = dofix(v[0])
        output.write(" ")
        output.write(nextbyte)
        output.write(" : ldc2_w\n")
      elif converted_byte == "15":
        v = f.read(1)
        if not v:
          print("ERROR")
          break
        nextbyte = dofix(v[0])
        output.write(" ")
        output.write(nextbyte)
        output.write(" : iload\n")
      elif converted_byte == "16":
        v = f.read(1)
        if not v:
          print("ERROR")
          break
        nextbyte = dofix(v[0])
        output.write(" ")
        output.write(nextbyte)
        output.write(" : lload\n")
      elif converted_byte == "17":
        v = f.read(1)
        if not v:
          print("ERROR")
          break
        nextbyte = dofix(v[0])
        output.write(" ")
        output.write(nextbyte)
        output.write(" : fload\n")
      elif converted_byte == "18":
        v = f.read(1)
        if not v:
          print("ERROR")
          break
        nextbyte = dofix(v[0])
        output.write(" ")
        output.write(nextbyte)
        output.write(" : dload\n")
      elif converted_byte == "19":
        v = f.read(1)
        if not v:
          print("ERROR")
          break
        nextbyte = dofix(v[0])
        output.write(" ")
        output.write(nextbyte)
        output.write(" : aload\n")
      elif converted_byte == "1A":
        output.write(" : iload_0\n")
      elif converted_byte == "1B":
        output.write(" : iload_1\n")
      elif converted_byte == "1C":
        output.write(" : iload_2\n")
      elif converted_byte == "1D":
        output.write(" : iload_3\n")
      elif converted_byte == "1E":
        output.write(" : lload_0\n")
      elif converted_byte == "1F":
        output.write(" : lload_1\n")
      elif converted_byte == "20":
        output.write(" : lload_2\n")
      elif converted_byte == "21":
        output.write(" : lload_3\n")
      elif converted_byte == "22":
        output.write(" : fload_0\n")
      elif converted_byte == "23":
        output.write(" : fload_1\n")
      elif converted_byte == "24":
        output.write(" : fload_2\n")
      elif converted_byte == "25":
        output.write(" : fload_3\n")
      elif converted_byte == "26":
        output.write(" : dload_0\n")
      elif converted_byte == "27":
        output.write(" : dload_1\n")
      elif converted_byte == "28":
        output.write(" : dload_2\n")
      elif converted_byte == "29":
        output.write(" : dload_3\n")
      elif converted_byte == "2A":
        output.write(" : aload_0\n")
      elif converted_byte == "2B":
        output.write(" : aload_1\n")
      elif converted_byte == "2C":
        output.write(" : aload_2\n")
      elif converted_byte == "2D":
        output.write(" : aload_3\n")
      elif converted_byte == "2E":
        output.write(" : iaload\n")
      elif converted_byte == "2F":
        output.write(" : laload\n")
      elif converted_byte == "30":
        output.write(" : faload\n")
      elif converted_byte == "31":
        output.write(" : daload\n")
      elif converted_byte == "32":
        output.write(" : aaload\n")
      elif converted_byte == "33":
        output.write(" : baload\n")
      elif converted_byte == "34":
        output.write(" : caload\n")
      elif converted_byte == "35":
        output.write(" : saload\n")
      elif converted_byte == "36":
        v = f.read(1)
        if not v:
          print("ERROR")
          break
        nextbyte = dofix(v[0])
        output.write(" ")
        output.write(nextbyte)
        output.write(" : istore\n")
      elif converted_byte == "37":
        v = f.read(1)
        if not v:
          print("ERROR")
          break
        nextbyte = dofix(v[0])
        output.write(" ")
        output.write(nextbyte)
        output.write(" : lstore\n")
      elif converted_byte == "38":
        v = f.read(1)
        if not v:
          print("ERROR")
          break
        nextbyte = dofix(v[0])
        output.write(" ")
        output.write(nextbyte)
        output.write(" : fstore\n")
      elif converted_byte == "39":
        v = f.read(1)
        if not v:
          print("ERROR")
          break
        nextbyte = dofix(v[0])
        output.write(" ")
        output.write(nextbyte)
        output.write(" : dstore\n")
      elif converted_byte == "3A":
        v = f.read(1)
        if not v:
          print("ERROR")
          break
        nextbyte = dofix(v[0])
        output.write(" ")
        output.write(nextbyte)
        output.write(" : astore\n")
      else:
        output.write(" : UNHANDLED BYTECODE\n")
