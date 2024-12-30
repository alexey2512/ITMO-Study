check(D, N) :-
    D * D > N,
    assert(min_divisor(N, N)).
check(D, N) :-
    D * D =< N,
    (0 =:= N mod D ->
        assert(min_divisor(D, N)),
        fail
        ;
        NEXT is D + 1,
        check(NEXT, N)).

assert_all(N, MAX, _) :- N > MAX.
assert_all(N, MAX, X) :-
    N =< MAX,
    (check(2, N) ->
        assert(prime(N)),
        assert(prime_index(N, X)),
        X1 is X + 1
        ;
        assert(composite(N)),
        X1 is X),
    N1 is N + 1,
    assert_all(N1, MAX, X1).

init(MAX) :- assert_all(2, MAX, 1), !.

multiply(1, []) :- !.
multiply(X, [H | T]) :-
    multiply(K, T),
    X is K * H.

is_normal([]).
is_normal([X]) :- prime(X).
is_normal([H, J | T]) :-
    H =< J,
    prime(H),
    is_normal([J | T]).

prime_divisors(1, []).
prime_divisors(N, [H | T]) :-
    (number(N) ->
        min_divisor(H, N),
        K is N // H,
        prime_divisors(K, T)
        ;
        multiply(N, [H | T]),
        is_normal([H | T])), !.
