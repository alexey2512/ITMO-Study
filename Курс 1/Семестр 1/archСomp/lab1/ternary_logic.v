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



module ternary_min(a[1:0], b[1:0], out[1:0]);

    input [1:0] a;
    input [1:0] b;
    output [1:0] out;

    wire a1_xor_a0, b1_xor_b0, xor_ab, nxor_ab, a0_or_b0;
    
    and_gate ans_for_o1(a[1], b[1], out[1]);

    xor_gate xor_a1a0(a[1], a[0], a1_xor_a0);
    xor_gate xor_b1b0(b[1], b[0], b1_xor_b0);
    xor_gate xor_all(a1_xor_a0, b1_xor_b0, xor_ab);
    not_gate nxor_all(xor_ab, nxor_ab);

    or_gate or_a0b0(a[0], b[0], a0_or_b0);
    and_gate ans_for_o0(nxor_ab, a0_or_b0, out[0]);

endmodule

module ternary_max(a[1:0], b[1:0], out[1:0]);

    input [1:0] a;
    input [1:0] b;
    output [1:0] out;

    wire disj1, na1, nb1, conj1;

    or_gate ans_for_o1(a[1], b[1], out[1]);
    or_gate a0_or_b0(a[0], b[0], disj1);

    not_gate not_a1(a[1], na1);
    not_gate not_b1(b[1], nb1);

    and_gate not_a1_and_not_b1(na1, nb1, conj1);
    and_gate ans_for_o0(conj1, disj1, out[0]);

endmodule

module ternary_any(a[1:0], b[1:0], out[1:0]);

    input [1:0] a;
    input [1:0] b;
    output [1:0] out;

    wire na0, nb0, na1, nb1;
    wire a1_xor_a0, b1_xor_b0, a1_and_na0, b1_and_nb0;
    wire conja, conjb;
    wire a1_xor_b1;
    wire conj0, conj1, conj2, conj3, conj4;
    
    not_gate not_a0(a[0], na0);
    not_gate nat_b0(b[0], nb0);
    not_gate not_a1(a[1], na1);
    not_gate nat_b1(b[1], nb1);

    xor_gate xor_a(a[1], a[0], a1_xor_a0);
    xor_gate xor_b(b[1], b[0], b1_xor_b0);

    and_gate and_a(a[1], na0, a1_and_na0);
    and_gate and_b(b[1], nb0, b1_and_nb0);

    and_gate conj_a(a1_and_na0, b1_xor_b0, conja);
    and_gate conj_b(b1_and_nb0, a1_xor_a0, conjb);

    or_gate ans_for_o1(conja, conjb, out[1]);
    
    xor_gate xor_ab1(a[1], b[1], a1_xor_b1);

    and_gate conj_nab0(na0, nb0, conj0);
    and_gate conj_nab1(na1, nb1, conj1);
    and_gate conj_ab0(a[0], b[0], conj2);
    and_gate conj_nab1ab0(conj1, conj2, conj3);
    and_gate conj_nab0_xor_ab1(conj0, a1_xor_b1, conj4);
    or_gate ans_for_o0(conj3, conj4, out[0]);

endmodule

module ternary_consensus(a[1:0], b[1:0], out[1:0]);

    input [1:0] a;
    input [1:0] b;
    output [1:0] out;
  
    wire na1, nb1;
    wire disj0, disj1, disj2, disj3, disj4;

    and_gate ans_for_o1(a[1], b[1], out[1]);

    not_gate not_a1(a[1], na1);
    not_gate not_b1(b[1], nb1);

    or_gate or_ab0(a[0], b[0], disj0);
    or_gate or_ab1(a[1], b[1], disj1);
    or_gate or_nab1(na1, nb1, disj2);
    or_gate or_ab0ab1(disj0, disj1, disj3);
    or_gate or_ab0nab1(disj0, disj2, disj4);
    and_gate ans_for_o0(disj3, disj4, out[0]);

endmodule 