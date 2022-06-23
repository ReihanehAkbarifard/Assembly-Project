import javax.swing.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        HashMap<String, String> registers32BitsOpCode = new HashMap<>();
        registers32BitsOpCode.put("eax", "000");
        registers32BitsOpCode.put("ecx", "001");
        registers32BitsOpCode.put("edx", "010");
        registers32BitsOpCode.put("ebx", "011");
        registers32BitsOpCode.put("esp", "100");
        registers32BitsOpCode.put("esi", "110");
        registers32BitsOpCode.put("edi", "111");
        registers32BitsOpCode.put("ebp", "101");

        HashMap<String, String> registers16BitsOpCode = new HashMap<>();
        registers16BitsOpCode.put("ax", "000");
        registers16BitsOpCode.put("cx", "001");
        registers16BitsOpCode.put("dx", "010");
        registers16BitsOpCode.put("bx", "011");
        registers16BitsOpCode.put("sp", "100");
        registers16BitsOpCode.put("si", "110");
        registers16BitsOpCode.put("di", "111");
        registers16BitsOpCode.put("bp", "101");

        HashMap<String, String> registers8BitsOpCode = new HashMap<>();
        registers8BitsOpCode.put("al", "000");
        registers8BitsOpCode.put("cl", "001");
        registers8BitsOpCode.put("dl", "010");
        registers8BitsOpCode.put("bl", "011");
        registers8BitsOpCode.put("ah", "100");
        registers8BitsOpCode.put("ch", "101");
        registers8BitsOpCode.put("dh", "110");
        registers8BitsOpCode.put("bh", "111");

        HashMap<String, String> registerToRegister32BitInstruction = new HashMap<>();
        registerToRegister32BitInstruction.put("add", "01");
        registerToRegister32BitInstruction.put("or", "09");
        registerToRegister32BitInstruction.put("and", "21");
        registerToRegister32BitInstruction.put("sub", "29");

        HashMap<String, String> registerToRegister8BitInstruction = new HashMap<>();
        registerToRegister8BitInstruction.put("add", "00");
        registerToRegister8BitInstruction.put("or", "08");
        registerToRegister8BitInstruction.put("and", "20");
        registerToRegister8BitInstruction.put("sub", "28");

        HashMap<String, String> memoryToRegister32Bit = new HashMap<>();
        memoryToRegister32Bit.put("add", "03");
        memoryToRegister32Bit.put("or", "0b");
        memoryToRegister32Bit.put("and", "23");
        memoryToRegister32Bit.put("sub", "2b");

        HashMap<String, String> memoryToRegister8Bit = new HashMap<>();
        memoryToRegister8Bit.put("add", "02");
        memoryToRegister8Bit.put("or", "0a");
        memoryToRegister8Bit.put("and", "22");
        memoryToRegister8Bit.put("sub", "2a");

        //final opCode
        String result = "";
        //give assembly code from user
        String string;

        do {
            string = JOptionPane.showInputDialog(null, "Please Enter The " +
                    "Input Or Enter '0' To Finish The Program", "Get Input",JOptionPane.QUESTION_MESSAGE);
            //split the input (the form of is input is like -> Add ax,bx . so operator1 is going to be
            //ax and operator2 is going to be bx, and instruction is going to be Add)
            //convert all cases to lower(because assembly is not case-sensitive)
            String[] parts = string.split(" ");
            //String opCode = instructionOpCode.get(parts[0].toLowerCase());

            String[] operators = parts[1].split(",");
            String operator1 = operators[0].toLowerCase();
            String operator2 = operators[1].toLowerCase();
            String instruction = parts[0].toLowerCase();

            //check if the first operator is memory or not. if it is memory we will swap that.
            String mod;
            int swap;
            if (operator1.charAt(0) == '['){
                String temp;
                temp = operator2;
                operator2 = operator1;
                operator1 = temp;
                //evaluate mod -> 00
                mod = "00";
                swap = 1;
                //call the memory method
                result = memoryToRegister(operator1, operator2, instruction, mod, swap, registers8BitsOpCode, registerToRegister32BitInstruction,
                        memoryToRegister32Bit, registers32BitsOpCode ,registerToRegister8BitInstruction ,registers16BitsOpCode, memoryToRegister8Bit);

            }
            else if (operator2.charAt(0) == '['){
                mod = "00";
                swap = 0;
                result = memoryToRegister(operator1, operator2, instruction, mod, swap, registers8BitsOpCode, registerToRegister32BitInstruction,
                        memoryToRegister32Bit, registers32BitsOpCode ,registerToRegister8BitInstruction ,registers16BitsOpCode, memoryToRegister8Bit);
            }
            //it's register
            else {
                //evaluate mod 11
                mod = "11";
                //call the memory function
                result = registerToRegister(operator1, operator2, instruction, mod, registers8BitsOpCode, registerToRegister32BitInstruction,
                        memoryToRegister32Bit, registers32BitsOpCode ,registerToRegister8BitInstruction ,registers16BitsOpCode, memoryToRegister8Bit) ;
            }
            JOptionPane.showMessageDialog(null,"The OpCode is : " + result,
                    "OpCode", JOptionPane.INFORMATION_MESSAGE);

        }while (!string.equals('0'));


    }
    //determine mod by passing 0 or 1

    public static String memoryToRegister(String operator1, String operator2, String instruction, String mod, int swap,
                                          HashMap<String, String> value8Bit,
                                          HashMap<String, String> oldOpCodesMemories16and32Bit,
                                          HashMap<String, String> opCodesMemories16and32Bit,
                                          HashMap<String, String> value32Bit, HashMap<String, String> oldOpCodesMemory8Bit,
                                          HashMap<String, String> value16Bit, HashMap<String, String> opCodesMemory8Bit) {
        String opCode = "";
        String updateMemoryOperator = "";
        //remove braces from operator
        updateMemoryOperator = operator2.substring(1, operator2.length() - 1);


        //if both 32 bit
        if (value32Bit.keySet().contains(operator1) && value32Bit.keySet().contains(updateMemoryOperator)) {
            //check that the swap has been happened or not.
            if (swap == 1)
                opCode += oldOpCodesMemories16and32Bit.get(instruction);
            else
                opCode += opCodesMemories16and32Bit.get(instruction);
            String modAndRegisterValue = "";
            modAndRegisterValue += mod;
            modAndRegisterValue += value32Bit.get(operator1);
            modAndRegisterValue += value32Bit.get(updateMemoryOperator);
            return finalOpCode(opCode, modAndRegisterValue);

        }
        //if both 16 bit(this is not mentioned in this part because of the exceptions)
        else if (value16Bit.keySet().contains(operator1) &&
               value16Bit.keySet().contains(updateMemoryOperator)) {
            return ("Not Supported");
        }
        //if both 8 bit
        else if (value8Bit.keySet().contains(operator1) && value8Bit.keySet().contains(updateMemoryOperator)){
            //check that the swap has been happened or not
            if (swap == 1)
                opCode += oldOpCodesMemory8Bit.get(instruction);
            else
                opCode += opCodesMemory8Bit.get(instruction);
            String modAndRegisterValue = "";
            modAndRegisterValue += mod;
            modAndRegisterValue += value8Bit.get(operator1);
            modAndRegisterValue += value8Bit.get(updateMemoryOperator);
        return finalOpCode(opCode, modAndRegisterValue);
    }

    else
        return("Not available");

    }

    public static String registerToRegister(String operator1, String operator2, String instruction, String mod,
                                    HashMap<String, String> Value8Bit,
                                    HashMap<String, String> oldOpCodesMemories16and32Bit,
                                    HashMap<String, String> opCodesMemories16and32Bit,
                                    HashMap<String, String> Value32Bit, HashMap<String, String> oldOpCodesMemory8Bit,
                                    HashMap<String, String> Value16Bit, HashMap<String, String> opCodesMemory8Bit){
        String opCode = "";
        //if both 32 bit
        if(Value32Bit.keySet().contains(operator1) && Value32Bit.keySet().contains(operator2)) {
            opCode += oldOpCodesMemories16and32Bit.get(instruction);
            String modAndRegisterValue = "";
            modAndRegisterValue += mod;
            modAndRegisterValue += Value32Bit.get(operator2);
            modAndRegisterValue += Value32Bit.get(operator1);
            return finalOpCode(opCode, modAndRegisterValue);
        }
        else if(Value16Bit.keySet().contains(operator1) && Value16Bit.keySet().contains(operator2)) {     //if both 16 bit(this is not mentioned in this part because of the exceptions)
            String prefix = "\\x66";
            opCode += oldOpCodesMemories16and32Bit.get(instruction);
            String modAndRegisterValue = "";
            modAndRegisterValue += mod;
            modAndRegisterValue += Value16Bit.get(operator2);
            modAndRegisterValue += Value16Bit.get(operator1);
            return (prefix + finalOpCode(opCode, modAndRegisterValue));
        }
        else if(Value8Bit.keySet().contains(operator1) && Value8Bit.keySet().contains(operator2)){     //if both 8 bit
            opCode += oldOpCodesMemory8Bit.get(instruction);
            String modAndRegisterValue = "";
            modAndRegisterValue += mod;
            modAndRegisterValue += Value8Bit.get(operator2);
            modAndRegisterValue += Value8Bit.get(operator1);

            return finalOpCode(opCode, modAndRegisterValue);
        }
        else
            return ("Not available");
    }
    //print the patter like \xopcode\xmod
    public static String finalOpCode(String opCode, String modAndRegisterValue){
        String finalOpCode = "\\x" + opCode + "\\";

        String temp = "x" + Integer.toHexString((Integer.parseInt(modAndRegisterValue,2)));


        int help = temp.substring(1).length();
        if (help < 2)
            temp = temp.charAt(0) + "0" + temp.substring(1);
        return finalOpCode + temp;
    }
}