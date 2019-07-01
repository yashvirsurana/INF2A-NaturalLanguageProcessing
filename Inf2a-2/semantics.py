# File: semantics.py
# Template file for Informatics 2A Assignment 2:
# 'A Natural Language Query System in Python/NLTK'

# John Longley, November 2012
# Revised November 2013 and November 2014 with help from Nikolay Bogoychev



# PART D: Semantics for the Query Language.

from agreement import *

def sem(tr):
    """translates a syntax tree into a logical lambda expression (in string form)"""
    rule = top_level_rule(tr)
    if (tr.label() == 'P'):
        return tr[0][0]
    elif (tr.label() == 'N'):
        return '(\\x.' + tr[0][0] + '(x))'  # \\ is escape sequence for \
    elif  # add code here
    
    elif (rule == 'AN -> A AN'):
        return '(\\x.(' + sem(tr[0]) + '(x) & ' + sem(tr[1]) + '(x)))'
    elif (rule == 'NP -> P'):
        return '(\\x.(x = ' + sem(tr[0]) + '))'
    elif  # add more code here


# Logic parser for lambda expressions

from nltk import LogicParser
lp = LogicParser()

# Lambda expressions can now be checked and simplified as follows:

#   A = lp.parse('(\\x.((\\P.P(x,x))(loves)))(John)')
#   B = lp.parse(sem(tr))  # for some tree tr
#   A.simplify()
#   B.simplify()


# Model checker

from nltk.sem.logic import *

# Can use: A.variable, A.term, A.term.first, A.term.second, A.function, A.args

def interpret_const_or_var(s,bindings,entities):
    if (s in entities): # s a constant
        return s
    else:               # s a variable
        return [p[1] for p in bindings if p[0]==s][0]  # finds most recent binding

def model_check (P,bindings,entities,fb):
    if (isinstance (P,ApplicationExpression)):
        if (len(P.args)==1):
            pred = P.function.__str__()
            arg = interpret_const_or_var(P.args[0].__str__(),bindings,entities)
            return fb.queryUnary(pred,arg)
        else:
            pred = P.function.function.__str__()
            arg0 = interpret_const_or_var(P.args[0].__str__(),bindings,entities)
            arg1 = interpret_const_or_var(P.args[1].__str__(),bindings,entities)
            return fb.queryBinary(pred,arg0,arg1)
    elif (isinstance (P,EqualityExpression)):
        arg0 = interpret_const_or_var(P.first.__str__(),bindings,entities)
        arg1 = interpret_const_or_var(P.second.__str__(),bindings,entities)
        return (arg0 == arg1)
    elif (isinstance (P,AndExpression)):
        return (model_check (P.first,bindings,entities,fb) and
                model_check (P.second,bindings,entities,fb))
    elif (isinstance (P,ExistsExpression)):
        v = str(P.variable)
        P1 = P.term
        for e in entities:
            bindings1 = [(v,e)] + bindings
            if (model_check (P1,bindings1,entities,fb)):
                return True
        return False

def find_all_solutions (L,entities,fb):
    v = str(L.variable)
    P = L.term
    return [e for e in entities if model_check(P,[(v,e)],entities,fb)]


# Interactive dialogue session

def fetch_input():
    s = raw_input('$$ ')
    while (s.split() == []):
        s = raw_input('$$ ')
    return s    

def output(s):
    print ('     '+s)

def dialogue():
    lx = Lexicon()
    fb = FactBase()
    output('')
    s = fetch_input()
    while (s.split() == []):
        s = raw_input('$$ ')
    while (s != 'exit'):
        if (s[-1]=='?'):
            sent = s[:-1] + ' ?'  # tolerate absence of space before '?'
            wds = sent.split()
            trees = all_valid_parses(lx,wds)
            if (len(trees)==0):
                output ("Eh??")
            elif (len(trees)>1):
                output ("Ambiguous!")
            else:
                tr = restore_words (trees[0],wds)
                lam_exp = lp.parse(sem(tr))
                L = lam_exp.simplify()
                # print L  # useful for debugging
                entities = lx.getAll('P')
                results = find_all_solutions (L,entities,fb)
                if (results == []):
                    if (wds[0].lower() == 'who'):
                        output ("No one")
                    else:
                        output ("None")
                else:
                    buf = ''
                    for r in results:
                        buf = buf + r + '  '
                    output (buf)
        else:
            if (s[-1]=='.'):
                s = s[:-1]  # tolerate final full stop
            wds = s.split()
            msg = process_statement(lx,wds,fb)
            if (msg == ''):
                output ("OK.")
            else:
                output ("Sorry - " + msg)
        s = fetch_input()

# End of PART D.
