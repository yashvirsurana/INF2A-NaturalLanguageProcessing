# File: pos_tagging.py
# Template file for Informatics 2A Assignment 2:
# 'A Natural Language Query System in Python/NLTK'

# John Longley, November 2012
# Revised November 2013 and November 2014 with help from Nikolay Bogoychev



# PART B: POS tagging

from statements import *

# The tagset we shall use is:
# P  A  Ns  Np  Is  Ip  Ts  Tp  BEs  BEp  DOs  DOp  AR  AND  WHO  WHICH  ?

# Tags for words playing a special role in the grammar:

function_words_tags = [('a','AR'), ('an','AR'), ('and','AND'),
     ('is','BEs'), ('are','BEp'), ('does','DOs'), ('do','DOp'), 
     ('who','WHO'), ('which','WHICH'), ('Who','WHO'), ('Which','WHICH'), ('?','?')]
     # upper or lowercase tolerated at start of question.

function_words = [p[0] for p in function_words_tags]

# English nouns with identical plural forms (list courtesy of Wikipedia):

unchanging_plurals = ['bison','buffalo','deer','fish','moose','pike','plankton',
     'salmon','sheep','swine','trout']


def noun_stem (s):
    """extracts the stem from a plural noun, or returns empty string"""    
    # add code here
    if (s in unchanging_plurals):
        return s
    if (s.endswith('men')):
        return s[:-2] + 'an'
    else:
        return verb_stem(s)

def tag_word (lx,wd):
    """returns a list of all possible tags for wd relative to lx"""
    # add code here
    list1 = []
    tagz = []

    if (wd in lx.getAll('P')):
        add(tagz,'P')
    if (wd in lx.getAll('A')):
        add(tagz,'A')
    
    lexicon_N = lx.getAll('N')
    if (wd in lexicon_N):
        add(tagz,'Ns')
    if (noun_stem (wd) in lexicon_N):
        add(tagz,'Np')

    lexicon_I = lx.getAll('I')
    if (wd in lexicon_I):
        add(tagz,'Ip')
    if (verb_stem(wd) in lexicon_I):
        add(tagz,'Is')

    lexicon_T = lx.getAll('T')
    if (wd in lexicon_T):
        add(tagz,'Tp')
    if (verb_stem(wd) in lexicon_T):
        add(tagz,'Ts')

    #N_singular = noun_stem (wd)
    #if (N_singular in lx.getAll('N')):
     #   add(tagz,'Ns')
    #if ((wd in lx.getAll('N')) and not(N_singular in lx.getAll('N'))):
     #   add(tagz,'Np')

    #if (wd in unchanging_plurals):
     #   add(tagz,'Ns')
      #  add(tagz,'Np')

    #I_singular = verb_stem(wd)
    #if (I_singular in lx.getAll('I')):
     #   add(tagz,'Ip')
    #if ((wd in lx.getAll('I')) and not(I_singular in lx.getAll('I'))):
     #   add(tagz,'Is')

    #T_singular = verb_stem(wd)
    #if (T_singular in lx.getAll('T')):
     #   add(tagz,'Tp')
    #if ((wd in lx.getAll('T')) and not(I_singular in lx.getAll('T'))):
     #   add(tagz,'Ts')
    for x in function_words_tags:
        if (wd == x[0]):
            add(tagz, x[1])
    
    return tagz


def tag_words (lx, wds):
    """returns a list of all possible taggings for a list of words"""
    if (wds == []):
        return [[]]
    else:
        tag_first = tag_word (lx, wds[0])
        tag_rest = tag_words (lx, wds[1:])
        return [[fst] + rst for fst in tag_first for rst in tag_rest]

# End of PART B.
