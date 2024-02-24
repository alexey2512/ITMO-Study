`include "util.v"

module control_unit(opc, fun, MRT, MWT, BNT, BET, AST, RDT, RWT, ACT, JPT, JLT, JRT);

input wire [5:0] opc, fun;
output wire MRT, MWT, BNT, BET, AST, RDT, RWT, JPT, JLT, JRT;
output reg [2:0] ACT;

reg mr, mw, bn, be, as, rd, rw, jp, jl, jr;
reg [1:0] ALUop;
reg a, c, d, e, f, na, nc, nd, ne, nf;

always @* begin

a = opc[5]; na = ~a;
c = opc[3]; nc = ~c;
d = opc[2]; nd = ~d;
e = opc[1]; ne = ~e;
f = opc[0]; nf = ~f;

rw = na & ne & nf & (nd | c & d) | e & f & nc & nd;
rd = na & nc & nd & ne & nf;
as = a & nd & e & f | na & c & ne & nf;
mr = a & nc & nd & e & f;
jl = na & nc & nd & e & f;
bn = na & nc & d & ne & f;
be = na & nc & d & ne & nf;
mw = a & c & nd & e & f;
jp = na & nc & nd & e;
ALUop[1] = na & nc & nd & e | na & ne & nf & (nc & nd | c & d);
ALUop[0] = na & d & ne & (nc | c & nf);
jr = 0;

if (ALUop == 2'b10)
begin
  case (fun)
  6'b100000: begin ACT = 3'b010; end
  6'b100010: begin ACT = 3'b110; end
  6'b100100: begin ACT = 3'b000; end
  6'b100101: begin ACT = 3'b001; end
  6'b101010: begin ACT = 3'b111; end
  6'b001000: begin jr = 1; rw = 0; end
  endcase
end else begin
  ACT[2] = (~ALUop[1]) & ALUop[0];
  ACT[1] = ~ALUop[1];
  ACT[0] = 0;
end
end

assign MRT = mr; assign MWT = mw;
assign RDT = rd; assign RWT = rw;
assign BNT = bn; assign BET = be;
assign AST = as;
assign JPT = jp;
assign JLT = jl;
assign JRT = jr; 

endmodule

module mips_cpu(clk, pc, pc_new, instruction_memory_a, instruction_memory_rd, data_memory_a, data_memory_rd, data_memory_we, data_memory_wd,
                register_a1, register_a2, register_a3, register_we3, register_wd3, register_rd1, register_rd2);
  // сигнал синхронизации
  input clk;
  // текущее значение регистра PC
  inout [31:0] pc;
  // новое значение регистра PC (адрес следующей команды)
  output [31:0] pc_new;
  // we для памяти данных
  output data_memory_we;
  // адреса памяти и данные для записи памяти данных
  output [31:0] instruction_memory_a, data_memory_a, data_memory_wd;
  // данные, полученные в результате чтения из памяти
  inout [31:0] instruction_memory_rd, data_memory_rd;
  // we3 для регистрового файла
  output register_we3;
  // номера регистров
  output [4:0] register_a1, register_a2, register_a3;
  // данные для записи в регистровый файл
  output [31:0] register_wd3;
  // данные, полученные в результате чтения из регистрового файла
  inout [31:0] register_rd1, register_rd2;

  wire [5:0] opcode = instruction_memory_rd[31:26], funct = instruction_memory_rd[5:0];
  wire [4:0] rs = instruction_memory_rd[25:21], rt = instruction_memory_rd[20:16], rd = instruction_memory_rd[15:11];
  wire [15:0] imm16 = instruction_memory_rd[15:0];
  wire [25:0] jumpAddr = instruction_memory_rd[25:0];
  wire MemtoReg, MemtoWrite, BN, BE, src, RegDst, Jump, Jal, Jr;
  wire [2:0] ctrl;
  wire [4:0] tempRegWrite;
  wire [31:0] imm32, reg2, res, value, pc0, pc1, tempPc1, tempPc2, tempJump, jmp, chetire;
  wire [31:0] reg1 = register_rd1;
  wire fobos, lol, kek, BranchChoose, notnol, rnol, PCSrc;

  control_unit CU(opcode, funct, MemtoReg, MemtoWrite, BN, BE, src, RegDst, register_we3, ctrl, Jump, Jal, Jr);

  mux2_5 mux243826342(rt, rd, RegDst, tempRegWrite);
  mux2_5 mux328423432(tempRegWrite, 5'b11111, Jal, register_a3);
  mux2_32 mux38273423(.d0(register_rd2), .d1(imm32), .a(src), .out(reg2));
  alu alu347242394322(reg1, reg2, ctrl, res, fobos);
  mux2_32 mux24382341(res, data_memory_rd, MemtoReg, value);
  mux2_32 mux38121921(value, pc0, Jal, register_wd3);

  shl_2 shl9474293231(imm32, chetire);
  alu alu929020002931(pc, 4, 3'b010, pc0, lol);
  alu alu192993476824(pc0, chetire, 3'b010, pc1, kek);
  mux2_1 mux923429213(BN, BE, fobos, BranchChoose);
  mux2_1 mux248919832(notnol, fobos, BE, rnol);
  and_gate and2382917(BranchChoose, rnol, PCSrc);
  mux2_32 mux92100646(pc0, pc1, PCSrc, tempPc1);
  shl_2 shl1287477777(tempJump, jmp);
  mux2_32 mux92347546(tempPc1, jmp, Jump, tempPc2);
  mux2_32 mux32742736(tempPc2, reg1, Jr, pc_new);

  assign tempJump = {6'b000000, jumpAddr};
  assign imm32 = {{16{imm16[15]}}, imm16};
  assign instruction_memory_a = pc;
  assign register_a1 = rs;
  assign register_a2 = rt;
  assign data_memory_a = res;
  assign data_memory_wd = register_rd2;
  assign data_memory_we = MemtoWrite;
  assign notnol = !fobos;
  
endmodule

module mux2_1(d0, d1, a, out);
  input d0, d1, a;
  output out;
  assign out = a ? d1 : d0;
endmodule

module and_gate(a, b, out);
  input a, b;
  output reg out;

  always @(a && b) begin
  out = a && b;
  end;
endmodule

module alu(a, b, control, res, f);
  input signed [31:0] a, b;
  input [2:0] control;
  output reg [31:0] res;
  output reg f;
  reg [31:0] bb;
  always @* begin
    bb = (control[2] == 0) ? b : ~b;
    case (control[1:0])
      2'b00: res = a & bb;
      2'b01: res = a | bb;
      2'b10: res = a + bb + control[2];
      2'b11: begin
        if (control[2] == 1) begin
          if (a < b) res = 1;
          else res = 0;
        end
      end
    endcase
    f = (res == 0);
  end
endmodule