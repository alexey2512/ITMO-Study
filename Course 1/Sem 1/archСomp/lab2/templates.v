module nand_gate(a, b, out);
  input wire a, b;
  output wire out;

  supply1 pwr;
  supply0 gnd;

  wire nmos1_out;

  pmos pmos1(out, pwr, a);
  pmos pmos2(out, pwr, b);

  nmos nmos1(nmos1_out, gnd, b);
  nmos nmos2(out, nmos1_out, a);
endmodule

module not_gate(a, out);
    input wire a;
    output wire out;

    nand_gate ng(a, a, out);
endmodule

module and_gate(a, b, out);
    input wire a, b;
    output wire out;

    wire nnd;

    nand_gate ng(a, b, nnd);
    not_gate nt(nnd, out);
endmodule

module or_gate(a, b, out);
    input wire a, b;
    output wire out;

    wire na, nb;

    not_gate nga(a, na);
    not_gate ngb(b, nb);
    nand_gate ng(na, nb, out);
endmodule

module xor_gate(a, b, out);
    input wire a, b;
    output wire out;

    wire na, nb, a_or_b, na_or_nb;

    not_gate not_a(a, na);
    not_gate not_b(b, nb);

    or_gate or_ab(a, b, a_or_b);
    or_gate or_nanb(na, nb, na_or_nb);

    and_gate ans_for_out(a_or_b, na_or_nb, out);
endmodule

module median(a, b, c, out);
  input wire a, b, c;
  output wire out;

  wire ab, ac, bc;
  wire ab_xor_ac;

  and_gate a_a_b(a, b, ab);
  and_gate a_a_c(a, c, ac);
  and_gate b_a_c(b, c, bc);

  xor_gate ab_x_ac(ab, ac, ab_xor_ac);
  xor_gate ab_xor_ac_xor_bc(ab_xor_ac, bc, out);
endmodule

module sum_block(a1, b1, c1, d_out, c_out);
  input wire a1, b1, c1;
  output wire d_out, c_out;

  wire a_xor_b;

  xor_gate a_x_b(a1, b1, a_xor_b);
  xor_gate a_x_b_x_c(a_xor_b, c1, d_out);
  median med(a1, b1, c1, c_out);
endmodule




module full_summator(a, b, out);
  input wire [3:0] a, b;
  output wire [3:0] out;

  wire c0, c1, c2, c3;

  sum_block sum0(a[0], b[0], 1'b0, out[0], c0);
  sum_block sum1(a[1], b[1], c0, out[1], c1);
  sum_block sum2(a[2], b[2], c1, out[2], c2);
  sum_block sum3(a[3], b[3], c2, out[3], c3);
endmodule

module bit_not(a, out);
  input wire [3:0] a;
  output wire [3:0] out;

  not_gate not0(a[0], out[0]);
  not_gate not1(a[1], out[1]);
  not_gate not2(a[2], out[2]);
  not_gate not3(a[3], out[3]);
endmodule

module bit_and(a, b, out);
  input wire [3:0] a, b;
  output wire [3:0] out;

  and_gate and0(a[0], b[0], out[0]);
  and_gate and1(a[1], b[1], out[1]);
  and_gate and2(a[2], b[2], out[2]);
  and_gate and3(a[3], b[3], out[3]);
endmodule

module bit_or(a, b, out);
  input wire [3:0] a, b;
  output wire [3:0] out;

  or_gate or0(a[0], b[0], out[0]);
  or_gate or1(a[1], b[1], out[1]);
  or_gate or2(a[2], b[2], out[2]);
  or_gate or3(a[3], b[3], out[3]);
endmodule

module bit_not_and(a, b, out);
  input wire [3:0] a, b;
  output wire [3:0] out;

  wire [3:0] temp;

  bit_and bitand(a, b, temp);
  bit_not bitnot(temp, out);
endmodule

module bit_not_or(a, b, out);
  input wire [3:0] a, b;
  output wire [3:0] out;

  wire [3:0] temp;

  bit_or bitor(a, b, temp);
  bit_not bitnot(temp, out);
endmodule

module opposite(a, out);
  input wire [3:0] a;
  output wire [3:0] out;

  wire [3:0] inverted;
  wire [3:0] one = 4'b0001;

  bit_not bitnot(a, inverted);
  full_summator sum(inverted, one, out);

endmodule

module slt(a, b, out);
  input wire [3:0] a, b;
  output wire [3:0] out;

  wire [3:0] a_minus_b;
  wire [3:0] minus_b;

  wire notb3, a_or_notb3, a_and_notb3, and1;

  opposite opp(b, minus_b);
  full_summator suma(a, minus_b, a_minus_b);

  not_gate nb3(b[3], notb3);
  or_gate aornb3(a[3], notb3, a_or_notb3);
  and_gate aandnb3(a[3], notb3, a_and_notb3);
  and_gate anddsaldksa(a_or_notb3, a_minus_b[3], and1);
  or_gate sdfsdj(and1, a_and_notb3, out[0]);

  not_gate shdgf(1'b1, out[1]);
  not_gate shdf(1'b1, out[2]);
  not_gate shf(1'b1, out[3]);

endmodule

module telp(c2, c1, c0, num, out);
  input wire c2, c1, c0;
  input wire [3:0] num;
  output wire [3:0] out;

  wire help1, c210;

  and_gate and111(c2, c1, help1);
  and_gate and222(c0, help1, c210);

  and_gate and333(c210, num[3], out[3]);
  and_gate and444(c210, num[2], out[2]);
  and_gate and555(c210, num[1], out[1]);
  and_gate and666(c210, num[0], out[0]);
endmodule

module alu(a, b, control, res);
  input [3:0] a, b;
  input [2:0] control;

  output [3:0] res;

  wire [3:0] inverted_b;
  wire [3:0] a_and_b, not_a_and_b, a_or_b, not_a_or_b, a_plus_b, a_minus_b, a_slt_b;
  wire [3:0] a_and_b1, not_a_and_b1, a_or_b1, not_a_or_b1, a_plus_b1, a_minus_b1, a_slt_b1;
  wire [3:0] sum12, sum34, sum56, sum1234, sum567;
  wire nc2, nc1, nc0;

  not_gate not111(control[2], nc2);
  not_gate not222(control[1], nc1);
  not_gate not333(control[0], nc0);

  bit_and aandb(a, b, a_and_b);
  bit_not_and naandb(a, b, not_a_and_b);
  bit_or aorb(a, b, a_or_b);
  bit_not_or naorb(a, b, not_a_or_b);
  full_summator aplusb(a, b, a_plus_b);
  opposite oppos(b, inverted_b);
  full_summator aminusb(a, inverted_b, a_minus_b);
  slt asltb(a, b, a_slt_b);

  telp tp1(nc2, nc1, nc0, a_and_b, a_and_b1);
  telp tp2(nc2, nc1, control[0], not_a_and_b, not_a_and_b1);
  telp tp3(nc2, control[1], nc0, a_or_b, a_or_b1);
  telp tp4(nc2, control[1], control[0], not_a_or_b, not_a_or_b1);
  telp tp5(control[2], nc1, nc0, a_plus_b, a_plus_b1);
  telp tp6(control[2], nc1, control[0], a_minus_b, a_minus_b1);
  telp tp7(control[2], control[1], nc0, a_slt_b, a_slt_b1);

  bit_or sm1(a_and_b1, not_a_and_b1, sum12);
  bit_or sm2(a_or_b1, not_a_or_b1, sum34);
  bit_or sm3(a_plus_b1, a_minus_b1, sum56);
  bit_or sm4(sum12, sum34, sum1234);
  bit_or sm5(sum56, a_slt_b1, sum567);
  bit_or sm6(sum1234, sum567, res);

endmodule






module d_latch(clk, d, we, q);
  input clk; // Сигнал синхронизации
  input d; // Бит для записи в ячейку
  input we; // Необходимо ли перезаписать содержимое ячейки

  output reg q; // Сама ячейка
  // Изначально в ячейке хранится 0
  initial begin
    q <= 0;
  end
  // Значение изменяется на переданное на спаде сигнала синхронизации
  always @ (negedge clk) begin
    // Запись происходит при we = 1
    if (we) begin
      q <= d;
    end
  end
endmodule

module register(clk, we_data, we, rd_data);
  input wire clk;
  input wire [3:0] we_data;
  input wire we;

  output wire [3:0] rd_data;

  d_latch strashno_ochen_strashno_mi_ne_znaem_chto_eto_takoel3(clk, we_data[3], we, rd_data[3]);
  d_latch strashno_ochen_strashno_mi_ne_znaem_chto_eto_takoel2(clk, we_data[2], we, rd_data[2]);
  d_latch strashno_ochen_strashno_mi_ne_znaem_chto_eto_takoel1(clk, we_data[1], we, rd_data[1]);
  d_latch strashno_ochen_strashno_mi_ne_znaem_chto_eto_takoel0(clk, we_data[0], we, rd_data[0]);

endmodule

module demux(a, out);
  input wire [1:0] a;
  output wire [3:0] out;

  wire na1, na0;

  not_gate not123(a[1], na1);
  not_gate not234(a[0], na0);

  and_gate ajaraguju3(a[1], a[0], out[3]);
  and_gate ajaraguju2(a[1], na0, out[2]);
  and_gate ajaraguju1(na1, a[0], out[1]);
  and_gate ajaraguju0(na1, na0, out[0]);
endmodule

module chebureck(rd3, rd2, rd1, rd0, q0, q1, q2, q3, out);
  input wire rd3, rd2, rd1, rd0, q0, q1, q2, q3;
  output wire out;

  wire rd3q0, rd2q1, rd1q2, rd0q3;
  wire sum32, sum10;

  and_gate and812213(rd3, q0, rd3q0);
  and_gate and8123242313(rd2, q1, rd2q1);
  and_gate and8122545455433(rd1, q2, rd1q2);
  and_gate and812243542343243213(rd0, q3, rd0q3);

  or_gate or2382238492(rd3q0, rd2q1, sum32);
  or_gate or2382243782(rd1q2, rd0q3, sum10);
  or_gate or3248278234(sum32, sum10, out);
endmodule

module register_mux(rd, k0, k1, k2, k3, out);
  input wire [1:0] rd;
  input wire [3:0] k0, k1, k2, k3;
  output wire [3:0] out;

  wire [3:0] rd_demuxed;

  demux dem123(rd, rd_demuxed);

  chebureck cheb2173812(rd_demuxed[3], rd_demuxed[2], rd_demuxed[1], rd_demuxed[0], k0[0], k1[0], k2[0], k3[0], out[0]);
  chebureck cheb2145312(rd_demuxed[3], rd_demuxed[2], rd_demuxed[1], rd_demuxed[0], k0[1], k1[1], k2[1], k3[1], out[1]);
  chebureck cheb4242312(rd_demuxed[3], rd_demuxed[2], rd_demuxed[1], rd_demuxed[0], k0[2], k1[2], k2[2], k3[2], out[2]);
  chebureck cheb2173452(rd_demuxed[3], rd_demuxed[2], rd_demuxed[1], rd_demuxed[0], k0[3], k1[3], k2[3], k3[3], out[3]);
endmodule

module register_file(clk, rd_addr, we_addr, we_data, rd_data, we);
  input clk; // Сигнал синхронизации
  input [1:0] rd_addr, we_addr; // Номера регистров для чтения и записи
  input [3:0] we_data; // Данные для записи в регистровый файл
  input we; // Необходимо ли перезаписать содержимое регистра

  output [3:0] rd_data; // Данные, полученные в результате чтения из регистрового файла

  wire [3:0] we_demuxed, we_help;
  wire not_we_addr_1, not_we_addr_0;
  wire [3:0] r0, r1, r2, r3;

  demux dem(we_addr, we_help);

  and_gate and7432343274(we, we_help[3], we_demuxed[3]);
  and_gate and7474835474(we, we_help[2], we_demuxed[2]);
  and_gate and7432567876(we, we_help[1], we_demuxed[1]);
  and_gate and7435443565(we, we_help[0], we_demuxed[0]);

  register reg0(clk, we_data, we_demuxed[3], r0);
  register reg1(clk, we_data, we_demuxed[2], r1);
  register reg2(clk, we_data, we_demuxed[1], r2);
  register reg3(clk, we_data, we_demuxed[0], r3);

  register_mux reg_mux(rd_addr, r0, r1, r2, r3, rd_data);

endmodule




module counter(clk, addr, control, immediate, data);
  input clk; // Сигнал синхронизации
  input [1:0] addr; // Номер значения счетчика которое читается или изменяется
  input [3:0] immediate; // Целочисленная константа, на которую увеличивается/уменьшается значение счетчика
  input control; // 0 - операция инкремента, 1 - операция декремента

  output [3:0] data; // Данные из значения под номером addr, подающиеся на выход

  wire[3:0] we_data, rd_data;
  wire [2:0] ctrl;

  register_file ya_pernul228(clk, addr, addr, we_data, rd_data, 1'b1);
  and_gate and24375235438694(1'b1, 1'b1, ctrl[2]);
  and_gate and43754947549592(1'b0, 1'b0, ctrl[1]);
  and_gate and98495942959284(control, control, ctrl[0]);
  alu alu1862949342934359643(rd_data, immediate, ctrl, we_data);

  assign data = rd_data;
endmodule


