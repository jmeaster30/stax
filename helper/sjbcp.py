#!/usr/bin/env python
import sys
import struct

if len(sys.argv) != 2:
  print("Whoops too many args :(")
  exit()

def dofix(b):
  return str(hex(b))[2:].rjust(2, '0').upper()

source = sys.argv[1]
jbc = []
with open(source, 'rb') as f:
  while 1:
    byte_s = f.read(1)
    if not byte_s:
      break
    byte = byte_s[0]
    jbc.append(byte)

with open(f"{source}.output", 'w') as output:
  constant_pool_count = 0
  constant_pool_size = 0
  interfaces_pool_count = 0
  interfaces_pool_size = 999999999
  field_pool_count = 0
  field_pool_size = 999999999
  method_pool_count = 0
  method_pool_size = 999999999
  attribute_pool_count = 0
  attribute_pool_size = 999999999
  idx = 0
  while idx < len(jbc):
    if idx == 0:
      output.write(dofix(jbc[idx]))
      output.write(" ")
      output.write(dofix(jbc[idx + 1]))
      output.write(" ")
      output.write(dofix(jbc[idx + 2]))
      output.write(" ")
      output.write(dofix(jbc[idx + 3]))
      output.write(" : MAGIC NUMBER\n")
      idx += 4
      continue
    if idx == 4:
      output.write(dofix(jbc[idx]))
      output.write(" ")
      output.write(dofix(jbc[idx + 1]))
      output.write(" : MINOR VERSION\n")
      idx += 2
      continue
    if idx == 6:
      output.write(dofix(jbc[idx]))
      output.write(" ")
      output.write(dofix(jbc[idx + 1]))
      output.write(" : MAJOR VERSION\n")
      idx += 2
      continue
    if idx == 8:
      output.write("\n")
      output.write(dofix(jbc[idx]))
      output.write(" ")
      output.write(dofix(jbc[idx + 1]))
      constant_pool_count = int.from_bytes(jbc[idx:idx+2], 'big') - 1
      output.write(f" : CONSTANT POOL COUNT ({constant_pool_count})\n")
      idx += 2
      continue
    if idx == 10:
      for cpidx in range(constant_pool_count):
        output.write("\n")
        tag = int.from_bytes(jbc[idx:idx+1], 'big')
        output.write(dofix(jbc[idx]))
        output.write(" : CP TAG")
        idx += 1
        if tag == 1:
          output.write(" - Utf8\n")
          length = int.from_bytes(jbc[idx:idx+2], 'big')
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx+1]))
          output.write(" - Utf8 Length\n")
          idx += 2
          for utf8idx in range(0, length):
            a = utf8idx + idx;
            output.write(dofix(jbc[a]))
            output.write(" ")
          output.write("- Utf8 Bytes\n")
          idx += length
        elif tag == 3:
          output.write(" - Integer\n")
          value = int.from_bytes(jbc[idx:idx+4], 'big')
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx+1]))
          output.write(" ")
          output.write(dofix(jbc[idx+2]))
          output.write(" ")
          output.write(dofix(jbc[idx+3]))
          output.write(" ")
          output.write(f"- Integer Value ({value})\n")
          idx += 4
        elif tag == 4:
          output.write(" - Float\n")
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx+1]))
          output.write(" ")
          output.write(dofix(jbc[idx+2]))
          output.write(" ")
          output.write(dofix(jbc[idx+3]))
          output.write(" ")
          output.write("- Float Value\n")
          idx += 4
        elif tag == 5:
          output.write(" - Long\n")
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx+1]))
          output.write(" ")
          output.write(dofix(jbc[idx+2]))
          output.write(" ")
          output.write(dofix(jbc[idx+3]))
          output.write(" ")
          output.write(dofix(jbc[idx+4]))
          output.write(" ")
          output.write(dofix(jbc[idx+5]))
          output.write(" ")
          output.write(dofix(jbc[idx+6]))
          output.write(" ")
          output.write(dofix(jbc[idx+7]))
          output.write(" ")
          output.write("- Long Value\n")
          idx += 8
        elif tag == 6:
          output.write(" - Double\n")
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx+1]))
          output.write(" ")
          output.write(dofix(jbc[idx+2]))
          output.write(" ")
          output.write(dofix(jbc[idx+3]))
          output.write(" ")
          output.write(dofix(jbc[idx+4]))
          output.write(" ")
          output.write(dofix(jbc[idx+5]))
          output.write(" ")
          output.write(dofix(jbc[idx+6]))
          output.write(" ")
          output.write(dofix(jbc[idx+7]))
          output.write(" ")
          output.write("- Double Value\n")
          idx += 8
        elif tag == 7:
          output.write(" - Class\n")
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx + 1]))
          output.write(" : NameIndex\n")
          idx += 2
        elif tag == 8:
          output.write(" - String\n")
        elif tag == 9:
          output.write(" - FieldRef\n")
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx + 1]))
          output.write(" : ClassIndex\n")
          idx += 2
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx + 1]))
          output.write(" : NameAndTypeIndex\n")
          idx += 2
        elif tag == 10:
          output.write(" - MethodRef\n")
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx + 1]))
          idx += 2
          output.write(" : ClassIndex\n")
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx + 1]))
          output.write(" : NameAndTypeIndex\n")
          idx += 2
        elif tag == 11:
          output.write(" - InterfaceMethodRef\n")
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx + 1]))
          output.write(" : ClassIndex\n")
          idx += 2
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx + 1]))
          output.write(" : NameAndTypeIndex\n")
          idx += 2
        elif tag == 12:
          output.write(" - NameAndType\n")
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx + 1]))
          output.write(" - NameIndex\n")
          idx += 2
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx + 1]))
          output.write(" - DescriptorIndex\n")
          idx += 2
        elif tag == 15:
          output.write(" - MethodHandle\n")
          output.write(dofix(jbc[idx]))
          output.write(" - ReferenceKind\n")
          idx += 1
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx + 1]))
          output.write(" - ReferenceIndex\n")
          idx += 2
        elif tag == 16:
          output.write(" - MethodType\n")
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx + 1]))
          output.write(" - DescriptorIndex\n")
          idx += 2
        elif tag == 17:
          output.write(" - Dynamic\n")
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx + 1]))
          output.write(" - BootstrapMethodAttrIndex\n")
          idx += 2
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx + 1]))
          output.write(" - NameAndTypeIndex\n")
          idx += 2
        elif tag == 18:
          output.write(" - InvokeDynamic\n")
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx + 1]))
          output.write(" - BootstrapMethodAttrIndex\n")
          idx += 2
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx + 1]))
          output.write(" - NameAndTypeIndex\n")
          idx += 2
        elif tag == 19:
          output.write(" - Module\n")
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx + 1]))
          output.write(" - NameIndex\n")
          idx += 2
        elif tag == 20:
          output.write(" - Package\n")
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx + 1]))
          output.write(" - NameIndex\n")
          idx += 2
        else:
          output.write(" - unknown\n")
      constant_pool_size = idx - 10
      continue
    if idx == 10 + constant_pool_size:
      output.write("\n")
      output.write(dofix(jbc[idx]))
      output.write(" ")
      output.write(dofix(jbc[idx + 1]))
      output.write(" : AccessFlags\n")
      idx += 2
      continue
    if idx == 12 + constant_pool_size:
      output.write(dofix(jbc[idx]))
      output.write(" ")
      output.write(dofix(jbc[idx + 1]))
      output.write(" : ThisClass\n")
      idx += 2
      continue
    if idx == 14 + constant_pool_size:
      output.write(dofix(jbc[idx]))
      output.write(" ")
      output.write(dofix(jbc[idx + 1]))
      output.write(" : SuperClass\n")
      idx += 2
      continue
    if idx == 16 + constant_pool_size:
      output.write("\n")
      output.write(dofix(jbc[idx]))
      output.write(" ")
      output.write(dofix(jbc[idx + 1]))
      interfaces_pool_count = int.from_bytes(jbc[idx:idx+2], 'big')
      output.write(f" : InterfacesCount ({interfaces_pool_count})\n")
      idx += 2
      continue
    if idx == 18 + constant_pool_size + interfaces_pool_size:
      output.write("\n")
      output.write(dofix(jbc[idx]))
      output.write(" ")
      output.write(dofix(jbc[idx + 1]))
      field_pool_count = int.from_bytes(jbc[idx:idx+2], 'big')
      output.write(f" : FieldCount ({interfaces_pool_count})\n")
      idx += 2
      continue
    if idx == 18 + constant_pool_size:
      for ipidx in range(interfaces_pool_count):
        output.write("\n")
        output.write(dofix(jbc[idx]))
        output.write(" ")
        output.write(dofix(jbc[idx + 1]))
        output.write(" : Interface\n")
        idx += 2
      interfaces_pool_size = idx - (18 + constant_pool_size)
      continue
    if idx == 20 + constant_pool_size + interfaces_pool_size + field_pool_size:
      output.write("\n")
      output.write(dofix(jbc[idx]))
      output.write(" ")
      output.write(dofix(jbc[idx + 1]))
      method_pool_count = int.from_bytes(jbc[idx:idx+2], 'big')
      output.write(f" : MethodCount ({method_pool_count})\n")
      idx += 2
      continue
    if idx == 20 + constant_pool_size + interfaces_pool_size:
      print("small fart")
      for ipidx in range(field_pool_count):
        output.write("\n")
        output.write(dofix(jbc[idx]))
        output.write(" ")
        output.write(dofix(jbc[idx + 1]))
        output.write(" - AccessFlags\n")
        idx += 2
        output.write(dofix(jbc[idx]))
        output.write(" ")
        output.write(dofix(jbc[idx + 1]))
        output.write(" - NameIndex\n")
        idx += 2
        output.write(dofix(jbc[idx]))
        output.write(" ")
        output.write(dofix(jbc[idx + 1]))
        output.write(" - DescriptorIndex\n")
        idx += 2
        output.write(dofix(jbc[idx]))
        output.write(" ")
        output.write(dofix(jbc[idx + 1]))
        field_attributes_count = int.from_bytes(jbc[idx:idx+2], 'big')
        output.write(" - AttributesCount\n")
        idx += 2
        for faidx in range(field_attributes_count):
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx + 1]))
          output.write(" ::: AccessFlags\n")
          idx += 2
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx + 1]))
          output.write(" ")
          output.write(dofix(jbc[idx + 2]))
          output.write(" ")
          output.write(dofix(jbc[idx + 3]))
          faa_count = int.from_bytes(jbc[idx:idx+4], 'big')
          output.write(f" :: AttrbutesLength ({faa_count})\n")
          idx += 4
          for faaidx in range(faa_count):
            output.write(dofix(jbc[idx]))
            output.write(f" :: Attribute ({faa_count})\n")
            idx += 1
          output.write("\n")
        idx += 2
      field_pool_size = idx - (20 + constant_pool_size + interfaces_pool_size)
      continue
    if idx == 22 + constant_pool_size + interfaces_pool_size + field_pool_size + method_pool_size:
      output.write(dofix(jbc[idx]))
      output.write(" ")
      output.write(dofix(jbc[idx + 1]))
      method_attributes_count = int.from_bytes(jbc[idx:idx+2], 'big')
      output.write(" : AttributesCount\n")
      idx += 2
      for faidx in range(method_attributes_count):
        output.write(dofix(jbc[idx]))
        output.write(" ")
        output.write(dofix(jbc[idx + 1]))
        output.write(" - NameIndex\n")
        idx += 2
        output.write(dofix(jbc[idx]))
        output.write(" ")
        output.write(dofix(jbc[idx + 1]))
        output.write(" ")
        output.write(dofix(jbc[idx + 2]))
        output.write(" ")
        output.write(dofix(jbc[idx + 3]))
        faa_count = int.from_bytes(jbc[idx:idx+4], 'big')
        output.write(f" :: AttributesLength ({faa_count})\n")
        idx += 4
        for faaidx in range(faa_count):
          output.write(dofix(jbc[idx]))
          output.write(f" :: Attribute ({faa_count})\n")
          idx += 1
        output.write("\n")
      continue
    if idx == 22 + constant_pool_size + interfaces_pool_size + field_pool_size:
      print("bug fart")
      for ipidx in range(method_pool_count):
        output.write("\n")
        output.write(dofix(jbc[idx]))
        output.write(" ")
        output.write(dofix(jbc[idx + 1]))
        output.write(" - AccessFlags\n")
        idx += 2
        output.write(dofix(jbc[idx]))
        output.write(" ")
        output.write(dofix(jbc[idx + 1]))
        output.write(" - NameIndex\n")
        idx += 2
        output.write(dofix(jbc[idx]))
        output.write(" ")
        output.write(dofix(jbc[idx + 1]))
        output.write(" - DescriptorIndex\n")
        idx += 2
        output.write(dofix(jbc[idx]))
        output.write(" ")
        output.write(dofix(jbc[idx + 1]))
        method_attributes_count = int.from_bytes(jbc[idx:idx+2], 'big')
        output.write(" - AttributesCount\n")
        idx += 2
        for faidx in range(method_attributes_count):
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx + 1]))
          output.write(" ::: NameIndex\n")
          idx += 2
          output.write(dofix(jbc[idx]))
          output.write(" ")
          output.write(dofix(jbc[idx + 1]))
          output.write(" ")
          output.write(dofix(jbc[idx + 2]))
          output.write(" ")
          output.write(dofix(jbc[idx + 3]))
          faa_count = int.from_bytes(jbc[idx:idx+4], 'big')
          output.write(f" :: AttributesLength ({faa_count})\n")
          idx += 4
          for faaidx in range(faa_count):
            output.write(dofix(jbc[idx]))
            output.write(f" :: Attribute ({faa_count})\n")
            idx += 1
          output.write("\n")
      method_pool_size = idx - (22 + constant_pool_size + interfaces_pool_size + field_pool_size)
      continue
    converted_byte = dofix(jbc[idx])
    idx += 1
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
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : bipush\n")
    elif converted_byte == "11":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : sipush\n")
    elif converted_byte == "12":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : ldc\n")
    elif converted_byte == "13":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : ldc_w\n")
    elif converted_byte == "14":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : ldc2_w\n")
    elif converted_byte == "15":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : iload\n")
    elif converted_byte == "16":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : lload\n")
    elif converted_byte == "17":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : fload\n")
    elif converted_byte == "18":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : dload\n")
    elif converted_byte == "19":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
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
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : istore\n")
    elif converted_byte == "37":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : lstore\n")
    elif converted_byte == "38":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : fstore\n")
    elif converted_byte == "39":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : dstore\n")
    elif converted_byte == "3A":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : astore\n")
    elif converted_byte == "3B":
      output.write(" : istore_0\n")
    elif converted_byte == "3C":
      output.write(" : istore_1\n")
    elif converted_byte == "3D":
      output.write(" : istore_2\n")
    elif converted_byte == "3E":
      output.write(" : istore_3\n")
    elif converted_byte == "3F":
      output.write(" : lstore_0\n")
    elif converted_byte == "40":
      output.write(" : lstore_1\n")
    elif converted_byte == "41":
      output.write(" : lstore_2\n")
    elif converted_byte == "42":
      output.write(" : lstore_3\n")
    elif converted_byte == "43":
      output.write(" : fstore_0\n")
    elif converted_byte == "44":
      output.write(" : fstore_1\n")
    elif converted_byte == "45":
      output.write(" : fstore_2\n")
    elif converted_byte == "46":
      output.write(" : fstore_3\n")
    elif converted_byte == "47":
      output.write(" : dstore_0\n")
    elif converted_byte == "48":
      output.write(" : dstore_1\n")
    elif converted_byte == "49":
      output.write(" : dstore_2\n")
    elif converted_byte == "4A":
      output.write(" : dstore_3\n")
    elif converted_byte == "4B":
      output.write(" : astore_0\n")
    elif converted_byte == "4C":
      output.write(" : astore_1\n")
    elif converted_byte == "4D":
      output.write(" : astore_2\n")
    elif converted_byte == "4E":
      output.write(" : astore_3\n")
    elif converted_byte == "4F":
      output.write(" : iastore\n")
    elif converted_byte == "50":
      output.write(" : lastore\n")
    elif converted_byte == "51":
      output.write(" : fastore\n")
    elif converted_byte == "52":
      output.write(" : dastore\n")
    elif converted_byte == "53":
      output.write(" : aastore\n")
    elif converted_byte == "54":
      output.write(" : bastore\n")
    elif converted_byte == "55":
      output.write(" : castore\n")
    elif converted_byte == "56":
      output.write(" : sastore\n")
    elif converted_byte == "57":
      output.write(" : pop\n")
    elif converted_byte == "58":
      output.write(" : pop2\n")
    elif converted_byte == "59":
      output.write(" : dup\n")
    elif converted_byte == "5A":
      output.write(" : dup_x1\n")
    elif converted_byte == "5B":
      output.write(" : dup_x2\n")
    elif converted_byte == "5C":
      output.write(" : dup2\n")
    elif converted_byte == "5D":
      output.write(" : dup2_x1\n")
    elif converted_byte == "5E":
      output.write(" : dup2_x2\n")
    elif converted_byte == "5F":
      output.write(" : swap\n")
    elif converted_byte == "60":
      output.write(" : iadd\n")
    elif converted_byte == "61":
      output.write(" : ladd\n")
    elif converted_byte == "62":
      output.write(" : fadd\n")
    elif converted_byte == "63":
      output.write(" : dadd\n")
    elif converted_byte == "64":
      output.write(" : isub\n")
    elif converted_byte == "65":
      output.write(" : lsub\n")
    elif converted_byte == "66":
      output.write(" : fsub\n")
    elif converted_byte == "67":
      output.write(" : dsub\n")
    elif converted_byte == "68":
      output.write(" : imul\n")
    elif converted_byte == "69":
      output.write(" : lmul\n")
    elif converted_byte == "6A":
      output.write(" : fmul\n")
    elif converted_byte == "6B":
      output.write(" : dmul\n")
    elif converted_byte == "6C":
      output.write(" : idiv\n")
    elif converted_byte == "6D":
      output.write(" : ldiv\n")
    elif converted_byte == "6E":
      output.write(" : fdiv\n")
    elif converted_byte == "6F":
      output.write(" : ddiv\n")
    elif converted_byte == "70":
      output.write(" : irem\n")
    elif converted_byte == "71":
      output.write(" : lrem\n")
    elif converted_byte == "72":
      output.write(" : frem\n")
    elif converted_byte == "73":
      output.write(" : drem\n")
    elif converted_byte == "74":
      output.write(" : ineg\n")
    elif converted_byte == "75":
      output.write(" : lneg\n")
    elif converted_byte == "76":
      output.write(" : fneg\n")
    elif converted_byte == "77":
      output.write(" : dneg\n")
    elif converted_byte == "78":
      output.write(" : ishl\n")
    elif converted_byte == "79":
      output.write(" : lshl\n")
    elif converted_byte == "7A":
      output.write(" : ishr\n")
    elif converted_byte == "7B":
      output.write(" : lshr\n")
    elif converted_byte == "7C":
      output.write(" : iushr\n")
    elif converted_byte == "7D":
      output.write(" : lushr\n")
    elif converted_byte == "7E":
      output.write(" : iand\n")
    elif converted_byte == "7F":
      output.write(" : land\n")
    elif converted_byte == "80":
      output.write(" : ior\n")
    elif converted_byte == "81":
      output.write(" : lor\n")
    elif converted_byte == "82":
      output.write(" : ixor\n")
    elif converted_byte == "83":
      output.write(" : lxor\n")
    elif converted_byte == "84":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : iinc\n")
    elif converted_byte == "85":
      output.write(" : i2l\n")
    elif converted_byte == "86":
      output.write(" : i2f\n")
    elif converted_byte == "87":
      output.write(" : i2d\n")
    elif converted_byte == "88":
      output.write(" : l2i\n")
    elif converted_byte == "89":
      output.write(" : l2f\n")
    elif converted_byte == "8A":
      output.write(" : l2d\n")
    elif converted_byte == "8B":
      output.write(" : f2i\n")
    elif converted_byte == "8C":
      output.write(" : f2l\n")
    elif converted_byte == "8D":
      output.write(" : f2d\n")
    elif converted_byte == "8E":
      output.write(" : d2i\n")
    elif converted_byte == "8F":
      output.write(" : d2l\n")
    elif converted_byte == "90":
      output.write(" : d2f\n")
    elif converted_byte == "91":
      output.write(" : i2b\n")
    elif converted_byte == "92":
      output.write(" : i2c\n")
    elif converted_byte == "93":
      output.write(" : i2s\n")
    elif converted_byte == "94":
      output.write(" : lcmp\n")
    elif converted_byte == "95":
      output.write(" : fcmpl\n")
    elif converted_byte == "96":
      output.write(" : fcmpg\n")
    elif converted_byte == "97":
      output.write(" : dcmpl\n")
    elif converted_byte == "98":
      output.write(" : dcmpg\n")
    elif converted_byte == "99":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : ifeq\n")
    elif converted_byte == "9A":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : ifne\n")
    elif converted_byte == "9B":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : iflt\n")
    elif converted_byte == "9C":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : ifge\n")
    elif converted_byte == "9D":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : ifgt\n")
    elif converted_byte == "9E":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : ifle\n")
    elif converted_byte == "9F":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : if_icmpeq\n")
    elif converted_byte == "A0":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : if_icmpne\n")
    elif converted_byte == "A1":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : if_icmplt\n")
    elif converted_byte == "A2":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : if_icmpge\n")
    elif converted_byte == "A3":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : if_icmpgt\n")
    elif converted_byte == "A4":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : if_icmple\n")
    elif converted_byte == "A5":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : if_acmpeq\n")
    elif converted_byte == "A6":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : if_acmpne\n")
    elif converted_byte == "A7":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : goto\n")
    elif converted_byte == "A8":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : jsr\n")
    elif converted_byte == "A9":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : ret\n")
    elif converted_byte == "AA":
      output.write(" : UNHANDLED BYTECODE\n")
    elif converted_byte == "AB":
      output.write(" : UNHANDLED BYTECODE\n")
    elif converted_byte == "AC":
      output.write(" : ireturn\n")
    elif converted_byte == "AD":
      output.write(" : lreturn\n")
    elif converted_byte == "AE":
      output.write(" : freturn\n")
    elif converted_byte == "AF":
      output.write(" : dreturn\n")
    elif converted_byte == "B0":
      output.write(" : areturn\n")
    elif converted_byte == "B1":
      output.write(" : return\n")
    elif converted_byte == "B2":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : getstatic\n")
    elif converted_byte == "B3":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : putstatic\n")
    elif converted_byte == "B4":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : getfield\n")
    elif converted_byte == "B5":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : putfield\n")
    elif converted_byte == "B6":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : invokevirtual\n")
    elif converted_byte == "B7":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : invokespecial\n")
    elif converted_byte == "B8":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : invokestatic\n")
    elif converted_byte == "B9":
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      if idx >= len(jbc):
        print("ERROR")
        break
      nextbyte = dofix(jbc[idx])
      idx += 1
      output.write(" ")
      output.write(nextbyte)
      output.write(" : invokeinterface\n")
    else:
      output.write(" : UNHANDLED BYTECODE\n")
