map_build(LM, TM) :-
    length(LM, Len),
    H is ceiling(log(Len + 1) / log(2)),
    build(H, LM, TM, []).

split([H | T], H, T).

build(_, [], nil, []) :- !.
build(1, [(K, V) | T], node(K, V, nil, nil), T) :- !.
build(H, LM, TM, T) :-
    CH is H - 1,
    build(CH, LM, Left, PR),
    PR = [], TM = Left, T = [].
build(H, LM, TM, T) :-
    CH is H - 1,
    build(CH, LM, Left, PR),
    split(PR, (K, V), RT),
    build(CH, RT, Right, T),
    TM = node(K, V, Left, Right).

map_get(node(K, V, _, _), K, V).
map_get(node(K, V, Left, _), Kk, Vv) :-
    Kk < K,
    map_get(Left, Kk, Vv).
map_get(node(K, V, _, Right), Kk, Vv) :-
    Kk > K,
    map_get(Right, Kk, Vv).

map_lastEntry(node(K, V, _, nil), (K, V)) :- !.
map_lastEntry(node(_, _, _, Right), E) :-
    map_lastEntry(Right, E).

map_lastKey(T, K) :- map_lastEntry(T, (K, _)).
map_lastValue(T, V) :- map_lastEntry(T, (_, V)).

map_replace(nil, _, _, nil) :- !.
map_replace(node(K, _, Left, Right), K, V, node(K, V, Left, Right)) :- !.
map_replace(node(Kk, Vv, Left, Right), K, V, R) :-
    K < Kk,
    map_replace(Left, K, V, NewLeft),
    R = node(Kk, Vv, NewLeft, Right).
map_replace(node(Kk, Vv, Left, Right), K, V, R) :-
    K > Kk,
    map_replace(Right, K, V, NewRight),
    R = node(Kk, Vv, Left, NewRight).