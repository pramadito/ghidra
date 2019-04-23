/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghidra.pdb.pdbreader.symbol;

import ghidra.pdb.PdbByteReader;
import ghidra.pdb.PdbException;
import ghidra.pdb.pdbreader.AbstractPdb;
import ghidra.pdb.pdbreader.CategoryIndex;

/**
 * This class represents Extended Internals 32 of the High Level Shader Language symbol.
 * <P>
 * Note: we have guessed that HLSL means High Level Shader Language.
 * <P>
 * Note: we do not necessarily understand each of these symbol type classes.  Refer to the
 *  base class for more information.
 */
public class DataHighLevelShaderLanguageSymbolInternals32Extended
		extends AbstractDataHighLevelShaderLanguageSymbolInternals {

	protected long registerIndex;
	protected long bindSpace;
	protected long bindSlot;

	/**
	 * Constructor for this symbol internals.
	 * @param pdb {@link AbstractPdb} to which this symbol belongs.
	 */
	public DataHighLevelShaderLanguageSymbolInternals32Extended(AbstractPdb pdb) {
		super(pdb);
	}

	@Override
	public void emit(StringBuilder builder) {
		builder.append(String.format(": Type: %s. %s\n", pdb.getTypeRecord(typeIndex),
			getRegisterType().toString()));
		builder.append(String.format(
			"   register index = %d, base data offset start = %d, bind space = %d, bind slot = %d\n",
			registerIndex, dataOffset, bindSpace, bindSlot));
	}

	@Override
	public void parse(PdbByteReader reader) throws PdbException {
		// Different order, items, and different sizes than for parent.
		typeIndex = reader.parseInt();
		pdb.pushDependencyStack(new CategoryIndex(CategoryIndex.Category.DATA, typeIndex));
		pdb.popDependencyStack();
		registerIndex = reader.parseUnsignedIntVal();
		dataOffset = reader.parseUnsignedIntVal();
		bindSpace = reader.parseUnsignedIntVal();
		bindSlot = reader.parseUnsignedIntVal();
		registerType = HLSLRegisterType.fromValue(
			reader.parseUnsignedShortVal());
		name.parse(reader);
	}

}
