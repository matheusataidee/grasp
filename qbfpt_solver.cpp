#include <bits/stdc++.h>
using namespace std;
typedef long long int ll;
typedef pair<int,int> pi;
#define MAX 100
#define INF 0x3f3f3f3f
bool vars[MAX];
int mat[MAX][MAX];
int n;
bool prohi[MAX][MAX];
double vecAux[MAX];
double globalBest;
int triples[MAX][3];
bool bvars[MAX];
int f(int i,int p1,int p2){
	return  1 + ((p1*i+p2)%n);
}
void mountProhibitedList(){
	for (int i = 0; i <n; i++){
		triples[i][0] = i+1;

		if (f(i, 131, 1031) != i+1){
			triples[i][1] = f(i, 131, 1031);
		} else {
			triples[i][1] = 1 + (f(i, 131, 1031) %n);
		}

		int x = 1 + (f(i, 193, 1093) %n);
		if (f(i, 193, 1093) != i+1 && f(i, 193, 1093) != triples[i][1]){
			triples[i][2] = f(i, 193, 1093);
		} else if (x != i+1 && x != triples[i][1]){
			triples[i][2] = x;
		} else {
			triples[i][2] = 1 + ((f(i, 193, 1093) + 1) %n);
		}
		int maxi = max(triples[i][0],max(triples[i][1], triples[i][2]));
		int mini = min(triples[i][0], min(triples[i][1], triples[i][2]));
		int middle = triples[i][0] + triples[i][1] + triples[i][2] - maxi - mini;
		triples[i][0] = mini-1;
		triples[i][1] = middle-1;
		triples[i][2] = maxi-1;
	}
}
double eval(){
	double aux = 0, sum = 0;
	for (int i = 0; i <n; i++) {
		for (int j = 0; j < n; j++) {
			aux += vars[j] * mat[i][j];
		}
		vecAux[i] = aux;
		sum += aux * vars[i];
		aux = 0;
	}
	return sum;
}
void solve2n(){
	ll lim = 1LL << 40;
	for(ll a = 0;a<=lim;a++){
		bool val = true;
		for(ll i = 0;i<40;i++){
			vars[i] = a&(1LL<<i);
			val = vars[triples[i][0]] + vars[triples[i][1]] + vars[triples[i][2]] < 3;
			if(!val) break;
		}
		if(!val) continue;
		/*double cur = eval();
		if(cur > globalBest){
			globalBest = cur;
			for(int j = 0;j<n;j++) bvars[j] = vars[j];
		}*/
		globalBest = max(eval(),globalBest);
	}
}
void solve(int i){	
	if(i == n){	
		for(int j = 0;j<n;j++){
			if(vars[triples[j][0]] + vars[triples[j][1]] + vars[triples[j][2]] == 3) return;
		}
		double cur = eval();
		if(cur > globalBest){
			globalBest = cur;
			printf("%lf\n",cur);
			//for(int j = 0;j<n;j++) bvars[j] = vars[j];
		}
		return;
	}
	
	vars[i] = true; 
	
	if(!(vars[triples[i][0]] + vars[triples[i][1]] + vars[triples[i][2]] == 3)){		
		solve(i+1);	
	} 
		
	vars[i] = false;
	solve(i+1);	
}
int main(){
	cin >> n;
	for(int i = 0;i<n;i++){
		for(int j = i;j<n;j++) cin >> mat[i][j];
	}
	mountProhibitedList();
	for(int i = 0;i<n;i++)
		printf("Estou em %d e sua tripla eh %d,%d,%d.\n",i+1,triples[i][0],triples[i][1],triples[i][2]);
	globalBest = -INF;
	solve(0);
	for(int i = 0;i<n;i++) printf("%2d ",bvars[i]);
	printf("\n");
	for(int i = 0;i<n;i++) printf("%2d ",i);
	printf("\n");
	cout << globalBest << '\n';
	return 0;
}

