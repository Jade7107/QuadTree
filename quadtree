#include<iostream>
const int power[21]={1,2,4,8,16,32,64,128,256,512,1024,2048,4096,8192,16384,32768,65536,131072,262144,524288,1048576};
//power[0]=1;power[1]=2;power[2]=4;power[3]=8;power[4]=16;power[5]=32;
//power[6]=64;power[7]=128;power[8]=256;power[9]=512;power[10]=1024;power[11]=2048;
//power[12]=4096;power[13]=8192;power[14]=16384;power[15]=32768;power[16]=65536;
//power[17]=131072;power[18]=262144;power[19]=524288;power[20]=1048576;

class quad_tree {
public:
    int data, s;
    quad_tree *q1, *q2, *q3, *q4;

    quad_tree(int n){
        this->data = 0;
        this->s = n;
        this->q1 = NULL;
        this->q2 = NULL;
        this->q3 = NULL;
        this->q4 = NULL;
    }

    ~quad_tree(){
        if(q1!=NULL){
            delete this->q1;
            delete this->q2;
            delete this->q3;
            delete this->q4;
            this->q1 = NULL;
            this->q2 = NULL;
            this->q3 = NULL;
            this->q4 = NULL;
        }
    }

    quad_tree(quad_tree const &Q){
        this->s = Q.s;
        this->data = Q.data;
        if(Q.q1==NULL){
            this->q1 = NULL;
            this->q2 = NULL;
            this->q3 = NULL;
            this->q4 = NULL;
        }
        else{
            this->q1 = new quad_tree(*(Q.q1));
            this->q2 = new quad_tree(*(Q.q2));
            this->q3 = new quad_tree(*(Q.q3));
            this->q4 = new quad_tree(*(Q.q4));
        }
    }

    void reduce(){
        if(this->q1->q1==NULL && this->q2->q1==NULL && this->q3->q1==NULL && this->q4->q1==NULL && this->q1->data==this->q2->data && this->q2->data==this->q3->data && this->q3->data==this->q4->data){
            this->data = this->q1->data;
            delete this->q1;
            delete this->q2;
            delete this->q3;
            delete this->q4;
            this->q1 = NULL;
            this->q2 = NULL;
            this->q3 = NULL;
            this->q4 = NULL;
        }
    }

    void set(int x1, int y1, int x2, int y2, int b){
        //std::cout<<power[this->s]<<std::endl;
        if (x1==0 && y1==0 && x2==power[this->s]-1 && y2==power[this->s]-1){
            this->data = b;
            if(q1!=NULL){
                delete this->q1;
                delete this->q2;
                delete this->q3;
                delete this->q4;
                this->q1 = NULL;
                this->q2 = NULL;
                this->q3 = NULL;
                this->q4 = NULL;
            }
            return;
        }
        if(q1==NULL){
            if(this->data == b){
                return;
            }
            this->q1 = new quad_tree(this->s-1);
            this->q1->data = this->data;
            this->q2 = new quad_tree(this->s-1);
            this->q2->data = this->data;
            this->q3 = new quad_tree(this->s-1);
            this->q3->data = this->data;
            this->q4 = new quad_tree(this->s-1);
            this->q4->data = this->data;
        }
        int mid = power[this->s-1];
        if(x1>=mid){
            if(y1>=mid){
                this->q4->set(x1-mid,y1-mid,x2-mid,y2-mid,b);
                this->reduce();
                return;
            }
            if(y2<mid){
                this->q2->set(x1-mid,y1,x2-mid,y2,b);
                this->reduce();
                return;
            }
            this->q2->set(x1-mid,y1,x2-mid,mid-1,b);
            this->q4->set(x1-mid,0,x2-mid,y2-mid,b);
            this->reduce();
            return;
        }
        if(x2<mid){
            if(y1>=mid){
                this->q3->set(x1,y1-mid,x2,y2-mid,b);
                this->reduce();
                return;
            }
            if(y2<mid){
                this->q1->set(x1,y1,x2,y2,b);
                this->reduce();
                return;
            }
            this->q1->set(x1,y1,x2,mid-1,b);
            this->q3->set(x1,0,x2,y2-mid,b);
            this->reduce();
            return;
        }
        if(y1>=mid){
            this->q3->set(x1,y1-mid,mid-1,y2-mid,b);
            this->q4->set(0,y1-mid,x2-mid,y2-mid,b);
            this->reduce();
            return;
        }
        if(y2<mid){
            this->q1->set(x1,y1,mid-1,y2,b);
            this->q2->set(0,y1,x2-mid,y2,b);
            this->reduce();
            return;
        }
        this->q1->set(x1,y1,mid-1,mid-1,b);
        this->q2->set(0,y1,x2-mid,mid-1,b);
        this->q3->set(x1,0,mid-1,y2-mid,b);
        this->q4->set(0,0,x2-mid,y2-mid,b);
        this->reduce();
        return;
    }

    int get(int x1, int y1){
        if(this->q1==NULL) return this->data;
        int mid = power[this->s-1];
        if(x1>=mid){
            if(y1>=mid) return this->q4->get(x1-mid,y1-mid);
            return this->q2->get(x1-mid,y1);
        }
        if(y1>=mid) return this->q3->get(x1,y1-mid);
        return this->q1->get(x1,y1);
    }

    int size(){
        return this->s;
    }

    void overlap(quad_tree const &Q){
        if(this->q1==NULL){
            if(Q.q1==NULL){
                if(Q.data==1){
                    this->data = 1;
                }
                return;
            }
            if(this->data==0){
                this->q1 = new quad_tree(*(Q.q1));
                this->q2 = new quad_tree(*(Q.q2));
                this->q3 = new quad_tree(*(Q.q3));
                this->q4 = new quad_tree(*(Q.q4));
            }
            return;
        }
        if(Q.q1==NULL){
            if(Q.data==1){
                this->data = 1;
                delete this->q1;
                delete this->q2;
                delete this->q3;
                delete this->q4;
                this->q1 = NULL;
                this->q2 = NULL;
                this->q3 = NULL;
                this->q4 = NULL;
            }
            return;
        }
        this->q1->overlap(*(Q.q1));
        this->q2->overlap(*(Q.q2));
        this->q3->overlap(*(Q.q3));
        this->q4->overlap(*(Q.q4));
        this->reduce();
        return;
    }

    void intersect(quad_tree &Q){
        if(this->q1==NULL){
            if(Q.q1==NULL){
                if(Q.data==0){
                    this->data = 0;
                }
                return;
            }
            if(this->data==1){
                this->q1 = new quad_tree(*(Q.q1));
                this->q2 = new quad_tree(*(Q.q2));
                this->q3 = new quad_tree(*(Q.q3));
                this->q4 = new quad_tree(*(Q.q4));
            }
            return;
        }
        if(Q.q1==NULL){
            if(Q.data==0){
                this->data = 0;
                delete this->q1;
                delete this->q2;
                delete this->q3;
                delete this->q4;
                this->q1 = NULL;
                this->q2 = NULL;
                this->q3 = NULL;
                this->q4 = NULL;
            }
            return;
        }
        this->q1->intersect(*(Q.q1));
        this->q2->intersect(*(Q.q2));
        this->q3->intersect(*(Q.q3));
        this->q4->intersect(*(Q.q4));
        this->reduce();
        return;
    }

    void complement(){
        //std::cout<<power[this->s]<<std::endl;
        if(this->q1==NULL){
            this->data=1-this->data;
            return;
        }
        this->q1->complement();
        this->q2->complement();
        this->q3->complement();
        this->q4->complement();
        return;
    }

    void resize(int m){
        if(m==this->s) return;
        if(m>this->s){
            this->s=m;
            if(this->q1!=NULL){
                this->q1->resize(m-1);
                this->q2->resize(m-1);
                this->q3->resize(m-1);
                this->q4->resize(m-1);
            }
            return;
        }
        if(this->q1!=NULL){
            if(m==0){
                long long int x = power[this->s];
                if(x*x<=2*this->m()) this->data=1;
                else this->data=0;
                delete this->q1;
                delete this->q2;
                delete this->q3;
                delete this->q4;
                this->q1 = NULL;
                this->q2 = NULL;
                this->q3 = NULL;
                this->q4 = NULL;
                this->s = 0;
                return;
            }
            this->q1->resize(m-1);
            this->q2->resize(m-1);
            this->q3->resize(m-1);
            this->q4->resize(m-1);
            this->reduce();
        }
        this->s = m;
        return;
    }

    long long int m(){
        if(q1!=NULL) return this->q1->m()+this->q2->m()+this->q3->m()+this->q4->m();
        if(this->data==1) {
            long long int x = power[this->s];
            return x*x;
        }
        return 0;
    }

    void extract(int x1, int y1, int m){
        quad_tree *q = new quad_tree(m);
        q->inext(x1,y1,m,*(this));
        this->equate(*q);
        return;
    }

    void equate(quad_tree const &Q){
        this->s = Q.s;
        this->data = Q.data;
        if(Q.q1==NULL){
            if(this->q1!=NULL){
                delete this->q1;
                delete this->q2;
                delete this->q3;
                delete this->q4;
                this->q1 = NULL;
                this->q2 = NULL;
                this->q3 = NULL;
                this->q4 = NULL;
            }
            return;
        }
        if(this->q1==NULL){
            this->q1 = new quad_tree(this->s-1);
            this->q2 = new quad_tree(this->s-1);
            this->q3 = new quad_tree(this->s-1);
            this->q4 = new quad_tree(this->s-1);
        }
        this->q1->equate(*(Q.q1));
        this->q2->equate(*(Q.q2));
        this->q3->equate(*(Q.q3));
        this->q4->equate(*(Q.q4));
        return;
    }

    void inext(int x1, int y1, int m, quad_tree &Q){
        int a = Q.getRect(x1,y1,x1+power[m]-1,y1+power[m]-1);
        if(a==2){
            this->q1 = new quad_tree(this->s-1);
            this->q2 = new quad_tree(this->s-1);
            this->q3 = new quad_tree(this->s-1);
            this->q4 = new quad_tree(this->s-1);
            this->q1->inext(x1,y1,m-1,Q);
            this->q2->inext(x1+power[m-1],y1,m-1,Q);
            this->q3->inext(x1,y1+power[m-1],m-1,Q);
            this->q4->inext(x1+power[m-1],y1+power[m-1],m-1,Q);
            return;
        }
        this->data = a;
        return;
    }

    int getRect(int x1, int y1, int x2, int y2){
        if(this->q1==NULL) return this->data;
        if(x1==0 && y1==0 && x2==power[this->s]-1 && y2==power[this->s]-1) return 2;
        int mid = power[this->s-1];
        if(x1>=mid){
            if(y1>=mid) return this->q4->getRect(x1-mid,y1-mid,x2-mid,y2-mid);
            if(y2<mid) return this->q2->getRect(x1-mid,y1,x2-mid,y2);
            int a = this->q2->getRect(x1-mid,y1,x2-mid,mid-1);
            if(a==2) return 2;
            int b = this->q4->getRect(x1-mid,0,x2-mid,y2-mid);
            if(b!=a) return 2;
            return a;
        }
        if(x2<mid){
            if(y1>=mid) return this->q3->getRect(x1,y1-mid,x2,y2-mid);
            if(y2<mid) return this->q1->getRect(x1,y1,x2,y2);
            int a = this->q1->getRect(x1,y1,x2,mid-1);
            if(a==2) return 2;
            int b = this->q3->getRect(x1,0,x2,y2-mid);
            if(b!=a) return 2;
            return a;
        }
        if(y1>=mid){
            int a = this->q3->getRect(x1,y1-mid,mid-1,y2-mid);
            if(a==2) return 2;
            int b = this->q4->getRect(0,y1-mid,x2-mid,y2-mid);
            if(b!=a) return 2;
            return a;
        }
        if(y2<mid){
            int a = this->q1->getRect(x1,y1,mid-1,y2);
            if(a==2) return 2;
            int b = this->q2->getRect(0,y1,x2-mid,y2);
            if(b!=a) return 2;
            return a;
        }
        int a = this->q1->getRect(x1,y1,mid-1,mid-1);
        if(a==2) return 2;
        int b = this->q2->getRect(0,y1,x2-mid,mid-1);
        if(b!=a) return 2;
        b = this->q3->getRect(x1,0,mid-1,y2-mid);
        if(b!=a) return 2;
        b = this->q4->getRect(0,0,x2-mid,y2-mid);
        if(b!=a) return 2;
        return a;
    }
    
    void print(){
        std::cout<<s<<" "<<data<<std::endl;
        if(q1!=NULL){
            q1->print();
            q2->print();
            q3->print();
            q4->print();
        }
    }
};
